package com.cumt.musicserver.util;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsumerInfo {

    private Integer id;

    private String username;

    private String avator;

    private String phoneNum;

    private String email;

    private String introduction;

    private String location;

    private LocalDate birth;

    private Integer sex;
}
