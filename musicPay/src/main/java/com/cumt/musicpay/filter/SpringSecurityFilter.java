package com.cumt.musicpay.filter;

import com.cumt.musicpay.domain.Consumer;
import com.cumt.musicpay.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class SpringSecurityFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();
    public static final String REQUEST_Authentication_HEADER="Authorization";
    public static final String CONSUMER_REDIS_ID="consumerId:";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getHeader(REQUEST_Authentication_HEADER);
        if (Objects.isNull(key)) {
            filterChain.doFilter(request, response);
            return;
        }
        Integer id;
        try {
            id = JwtUtil.decodeJWTId(key);
        }catch (Exception e){
//            e.printStackTrace();
            filterChain.doFilter(request, response);
            return;
//            throw new RuntimeException("登录信息非法");
        }
        String json = stringRedisTemplate.opsForValue().get(CONSUMER_REDIS_ID + id);
        if (Objects.isNull(json)) {
            filterChain.doFilter(request, response);
            return;
//            throw new RuntimeException("登录过期");
        }
        stringRedisTemplate.opsForValue().set(CONSUMER_REDIS_ID + id, json, 30, TimeUnit.MINUTES);
        objectMapper.registerModule(new JavaTimeModule());
        Consumer consumer = objectMapper.readValue(json, Consumer.class);
        List<GrantedAuthority> list=new ArrayList<>();
        consumer.getRoles().forEach(each->{
            list.add(new SimpleGrantedAuthority(each));
        });
        UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(consumer,null,list);
        SecurityContextHolder.getContext().setAuthentication(u);
        filterChain.doFilter(request,response);
    }
}
