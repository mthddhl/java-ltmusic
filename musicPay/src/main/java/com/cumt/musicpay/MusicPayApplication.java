package com.cumt.musicpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;

@SpringBootApplication
@MapperScan("com.cumt.musicpay.dao")
@EnableScheduling
public class MusicPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicPayApplication.class, args);
    }

}
