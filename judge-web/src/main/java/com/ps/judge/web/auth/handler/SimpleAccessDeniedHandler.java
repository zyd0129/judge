package com.ps.judge.web.auth.handler;

import com.alibaba.fastjson.JSONObject;
import com.ps.common.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ApiResponse apiResponse = ApiResponse.error(403,"没有权限访问");
        response.getWriter().print(JSONObject.toJSON(apiResponse));
    }
}
