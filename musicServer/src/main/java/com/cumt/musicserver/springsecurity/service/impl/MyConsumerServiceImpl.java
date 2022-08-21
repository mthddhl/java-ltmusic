package com.cumt.musicserver.springsecurity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.dao.ConsumerDao;
import com.cumt.musicserver.dao.RoleDao;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.springsecurity.service.MyConsumerService;
import com.cumt.musicserver.util.JwtUtil;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class MyConsumerServiceImpl extends ServiceImpl<ConsumerDao, Consumer> implements MyConsumerService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Consumer one = lambdaQuery().eq(Consumer::getUsername, username).one();
        if(Objects.isNull(one)){
            throw new RuntimeException(StaticString.LOGIN_ERROR);
        }
        List<String> list=roleDao.getRolesById(one.getId());
        one.setRoles(list);
        return one;
    }

    public Result login(Consumer consumer) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(consumer.getUsername(),consumer.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException(StaticString.LOGIN_ERROR);
        }
        Consumer res= (Consumer) authenticate.getPrincipal();
        String jwt = JwtUtil.createJWT(res);
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            res.setPassword(null);res.setLocation(null);
            String s = objectMapper.writeValueAsString(res);
            stringRedisTemplate.opsForValue().set(StaticString.CONSUMER_REDIS_ID +res.getId(),s,30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.ok(jwt);
    }


    @Override
    public Result consumerLogout() {
        Object c = SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(!(c instanceof Consumer)){
            return Result.ok();
        }
        Consumer c1=(Consumer) c;
        Integer id = c1.getId();
        stringRedisTemplate.delete(StaticString.CONSUMER_REDIS_ID+id);
        return Result.ok();
    }

    @Override
    public Result consumerIsLogin() {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer res=lambdaQuery().eq(Consumer::getId,o.getId()).one();
        List<String> list=roleDao.getRolesById(res.getId());
        res.setRoles(list);
        String jwt = JwtUtil.createJWT(res);
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            res.setPassword(null);res.setLocation(null);
            String s = objectMapper.writeValueAsString(res);
            stringRedisTemplate.opsForValue().set(StaticString.CONSUMER_REDIS_ID +res.getId(),s,30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.ok(jwt);
    }
}
