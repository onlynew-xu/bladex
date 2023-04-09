package com.steelman.iot.platform.security.handler;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.push.GtPushApi;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import com.steelman.iot.platform.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author tang
 * date 2020-11-20
 */
public class SysUserLogoutHandler implements LogoutHandler {

    private RedisTemplate redisTemplate;

    public SysUserLogoutHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            //从redis移除token
            String userName = TokenUtils.getUserName(token);
            redisTemplate.delete(userName);
            String nameStr = userName.split("-")[0];
            String cid = TokenUtils.getCid(token);
            if (!StringUtils.isEmpty(cid)) {
                Object aliObj = redisTemplate.opsForHash().get("PUSH_" + nameStr, cid);
                GtPushApi.unbindAlias(cid, aliObj.toString());
                redisTemplate.opsForHash().delete("PUSH_" + nameStr, cid);
            }
            redisTemplate.delete(nameStr + "_user:userId");
        }
        Result<String> result = new Result<>();
        result.setCode(200);
        result.setMsg("注销成功");
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String res = JsonUtils.toJson(result, new TypeToken<Result<String>>() {
            }.getType());
            out.write(res.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("输出流关闭失败");
        }

    }

}
