package com.cumt.musicserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.cumt.musicserver.domain.Consumer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class dateTest {
//    public static void main(String[] args) {
//        Consumer c=new Consumer();
//        c.setId(11);;
//        c.setAvator("456486");
//        c.setUsername("zhangsan");
//        List<String> list=new ArrayList<String>();
//        list.add("admin");
//        list.add("consumer");
//        String sign = JWT.create().withClaim("id", c.getId())
//                .withClaim("roles", list).sign(Algorithm.HMAC256("132"));
//        System.out.println(sign);
//        Verification jwtVerifier=JWT.require(Algorithm.HMAC256("132"));
//        DecodedJWT decodedJWT=jwtVerifier.build().verify(sign);
//        System.out.println(decodedJWT.getClaim("id"));
//        List<String> list1 = decodedJWT.getClaim("roles").asList(String.class);
//        list1.forEach(System.out::println);
//
//    }
    public static void main(String[] args) {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode("123"));
//        System.out.println(bCryptPasswordEncoder.matches("123",
//                "$2a$10$oKeM9esxDtOx4dGfbr4dH.KfzzmNAOYYZ9E2Ke9mJsGJWV4hfZzWK"));
        Random r=new Random();
        System.out.println(r.nextInt(2)+1);
    }


}
