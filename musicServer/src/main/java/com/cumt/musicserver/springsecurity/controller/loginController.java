package com.cumt.musicserver.springsecurity.controller;

import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.springsecurity.service.MyConsumerService;
import com.cumt.musicserver.springsecurity.service.impl.MyConsumerServiceImpl;
import com.cumt.musicserver.util.JwtUtil;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/loginController")
public class loginController {
    @Resource
    private MyConsumerService consumerService;

    @PostMapping("/login")
    public Result consumerLogin(Consumer consumer){
        return consumerService.login(consumer);
    }
    @GetMapping("/consumerLogout")
    public Result consumerLogout(){
        return consumerService.consumerLogout();
    }

    @GetMapping("/consumerIsLogin")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerIsLogin(){
        return consumerService.consumerIsLogin();
    }

}
