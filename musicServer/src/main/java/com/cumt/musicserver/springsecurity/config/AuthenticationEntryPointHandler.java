package com.cumt.musicserver.springsecurity.config;

import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private ObjectMapper objectMapperNoTime=new ObjectMapper();


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        String s=authException.getMessage();
        response.setStatus(401);
//        ObjectMapper objectMapper=new ObjectMapper();
        Result fail = Result.fail("您未登录或登录信息失效");
        if(StaticString.LOGIN_ERROR.equals(authException.getMessage())){
            fail = Result.fail(StaticString.LOGIN_ERROR);
        }
        String result = objectMapperNoTime.writeValueAsString(fail);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(result);
    }
}
