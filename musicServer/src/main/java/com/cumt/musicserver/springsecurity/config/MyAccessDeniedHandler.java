package com.cumt.musicserver.springsecurity.config;

import com.cumt.musicserver.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapperNoTime=new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403);
//        ObjectMapper objectMapper=new ObjectMapper();
        Result fail = Result.fail("未登录或权限不足");
        String result = objectMapperNoTime.writeValueAsString(fail);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(result);
    }
}
