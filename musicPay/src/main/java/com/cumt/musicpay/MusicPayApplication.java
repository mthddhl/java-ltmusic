package com.cumt.musicpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cumt.musicpay.dao")
@EnableScheduling
public class MusicPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicPayApplication.class, args);
    }

}
