package com.steelman.iot.platform.security.filter;

import com.steelman.iot.platform.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tang
 * date 2020-11-22
 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public JwtFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("url ===>{},method ===>{}", requestURI,request.getMethod());
        //验证登录接口 直接放行
        if(requestURI.equals("/api/login")){
            filterChain.doFilter(request,response);
            return;
        }
        //获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        //判断如果有权限信息，放到权限上下文中
        if(authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }else{
            //判断如果没有权限信息,清空上下文登录信息
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request,response);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        if(StringUtils.isNotBlank(token)) {
            //从token获取用户名
            token=token.replace(" ","");
            String username = null;
            try {
                username = TokenUtils.getUserName(token);
            } catch (Exception e) {
                logger.error("token解析出错"+e.getMessage());
                return null;
            }
            //从redis获取对应权限列表
            Boolean aBoolean = redisTemplate.hasKey(username);
            if(!aBoolean){
               return null;
            }
            //刷新一个key的过期时间
            redisTemplate.expire(username,30, TimeUnit.MINUTES);
            List<String> roleList = (List<String>)redisTemplate.opsForValue().get(username);
            Collection<GrantedAuthority> authority = new ArrayList<>();
            if(roleList!=null){
                for(String role : roleList) {
                    SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role);
                    authority.add(auth);
                }
            }
            return new UsernamePasswordAuthenticationToken(username,token,authority);
        }
        return null;
    }
}
