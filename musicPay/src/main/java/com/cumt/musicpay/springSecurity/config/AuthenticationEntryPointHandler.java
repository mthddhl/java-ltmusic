package com.cumt.musicpay.springSecurity.config;


import com.cumt.musicpay.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private ObjectMapper objectMapperNoTime=new ObjectMapper();

    public static final String LOGIN_ERROR="用户名或密码错误";
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        String s=authException.getMessage();
        response.setStatus(401);
//        ObjectMapper objectMapper=new ObjectMapper();
        Result fail = Result.fail("您未登录或登录信息失效");
        if(LOGIN_ERROR.equals(authException.getMessage())){
            fail = Result.fail(LOGIN_ERROR);
        }
        String result = objectMapperNoTime.writeValueAsString(fail);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(result);
    }
}
