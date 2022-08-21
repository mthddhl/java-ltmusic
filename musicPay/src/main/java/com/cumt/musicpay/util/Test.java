package com.cumt.musicpay.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        System.out.println(now.isBefore(LocalDateTime.now()));

    }
}
