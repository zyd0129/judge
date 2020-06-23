package com.ps.judge.web.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.ps.common.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        ApiResponse apiResponse = ApiResponse.error(403, authException.getMessage());
        response.getWriter().print(JSONObject.toJSON(apiResponse));
    }
}
