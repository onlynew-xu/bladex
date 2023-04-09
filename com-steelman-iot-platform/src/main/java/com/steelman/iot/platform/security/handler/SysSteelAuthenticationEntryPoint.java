package com.steelman.iot.platform.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steelman.iot.platform.utils.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author tang
 * date 2020-11-20
 *  未认证处理器
 */
@Component
public class SysSteelAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        Result<String> result = new Result<>();
        result.setCode(401);
        result.setMsg("未认证访问失败.....");
        if (authException instanceof InsufficientAuthenticationException) {
            result.setMsg("请先登录");
        }
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();

    }
}
