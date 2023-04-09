package com.steelman.iot.platform.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steelman.iot.platform.utils.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author tang
 * date 2020-11-20
 */
@Component
public class SysUserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        Result<String> result = new Result<>();
        result.setCode(402);
        result.setMsg("权限不足...拒绝访问!!!");
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}
