package com.steelman.iot.platform.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.LogLogin;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.push.GtPushApi;
import com.steelman.iot.platform.security.entity.UserTokenEntity;
import com.steelman.iot.platform.security.exception.ParamException;
import com.steelman.iot.platform.security.exception.PostSupportServiceException;
import com.steelman.iot.platform.security.user.SysUser;
import com.steelman.iot.platform.service.LogLoginService;
import com.steelman.iot.platform.utils.IPUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import com.steelman.iot.platform.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author tang
 * date 2020-11-20
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    private LogLoginService logLoginService;

    public LoginFilter(RedisTemplate redisTemplate, LogLoginService logLoginService) {
        this.redisTemplate = redisTemplate;
        this.logLoginService = logLoginService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new PostSupportServiceException(
                    "MUST POST");
        }
        Map<String, String> loginData = new HashMap<>();
        try {
            loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParamException(
                    "Param  not supported: ");
        }
        String username = loginData.get(getUsernameParameter());
        String password = loginData.get(getPasswordParameter());
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        request.setAttribute("cid", loginData.get("cid"));
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        request.getSession();
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = response.getOutputStream();
        SysUser sysUser = (SysUser) authResult.getPrincipal();
        User currentUser = sysUser.getCurrentUser();

        Object cidObj = request.getAttribute("cid");
        if (!Objects.isNull(cidObj)) {
            String alias = currentUser.getUsername() + "_" + System.currentTimeMillis();
            redisTemplate.opsForHash().put("PUSH_" + currentUser.getUsername(), cidObj.toString(), alias);
            GtPushApi.bindAlias(cidObj.toString(), alias);
        }
        //生成token
        String usernameUid = currentUser.getUsername() + "-" + UUID.randomUUID().toString();
        String token = TokenUtils.createToken(currentUser.getId(), TokenUtils.nextKey(currentUser.getId()), usernameUid, Objects.isNull(cidObj) ? "" : cidObj.toString());
        //生成保存日志
        LogLogin logLogin = new LogLogin();
        logLogin.setUserId(currentUser.getId());
        logLogin.setIp(IPUtils.getIpAddr(request));
        logLogin.setUsername(currentUser.getUsername());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logLogin.setLoginTime(simpleDateFormat.format(date));
        logLogin.setCreateTime(date);
        logLogin.setUpdateTime(date);
        logLoginService.saveLoginLog(logLogin);
        List<String> rolesList = new ArrayList<>();
        //初始化redis存入两个字段
        redisTemplate.opsForValue().set(usernameUid, rolesList, 30, TimeUnit.MINUTES);
        String usernameKey = currentUser.getUsername() + "_user:userId";
        //直接覆盖redisKey
        redisTemplate.opsForValue().set(usernameKey, currentUser.getId(), 3, TimeUnit.HOURS);
        Result<UserTokenEntity> tokenResult = new Result<>();
        tokenResult.setMsg("获取token信息成功");
        tokenResult.setCode(200);
        UserTokenEntity userTokenEntity = new UserTokenEntity(currentUser.getId(), token);
        tokenResult.setData(userTokenEntity);
        String res = JsonUtils.toJson(tokenResult, new TypeToken<Result<UserTokenEntity>>() {
        }.getType());
        out.write(res.getBytes());
        out.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = response.getOutputStream();
        Result<String> result = new Result<>();
        //处理异常
        if (exception instanceof LockedException) {
            result.setCode(501);
            result.setMsg("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            result.setCode(502);
            result.setMsg("密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            result.setCode(503);
            result.setMsg("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            result.setCode(504);
            result.setMsg("账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException) {
            result.setCode(505);
            result.setMsg("用户名或者密码输入错误，请重新输入!");
        } else if (exception instanceof PostSupportServiceException) {
            result.setCode(506);
            result.setMsg("请使用post 提交");
        } else if (exception instanceof ParamException) {
            result.setCode(507);
            result.setMsg("参数错误");
        } else if (exception instanceof UsernameNotFoundException) {
            result.setCode(508);
            result.setMsg("用户不存在");
        }
        out.write(JsonUtils.toJson(result, new TypeToken<Result<String>>() {
        }.getType()).getBytes());
        out.close();
    }

}
