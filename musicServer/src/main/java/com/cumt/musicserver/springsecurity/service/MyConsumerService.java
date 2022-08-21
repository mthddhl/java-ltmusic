package com.cumt.musicserver.springsecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.util.Result;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyConsumerService extends IService<Consumer>,UserDetailsService {
    Result login(Consumer consumer);

    Result consumerLogout();

    Result consumerIsLogin();
}
