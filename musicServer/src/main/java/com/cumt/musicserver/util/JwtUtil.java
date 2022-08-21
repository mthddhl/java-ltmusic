package com.cumt.musicserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.cumt.musicserver.domain.Consumer;

import java.util.List;

public class JwtUtil {
    static Verification jwtVerifier=JWT.require(Algorithm.HMAC256("123"));
    public static String createJWT(Consumer c){
        String sign = JWT.create().withClaim("id", c.getId())
                .withClaim("roles",c.getRoles())
                .withClaim("avator",c.getAvator())
                .withClaim("vipExpireTime", String.valueOf(c.getVipExpireTime()))
                .sign(Algorithm.HMAC256("123"));
        return sign;
    }

    public static Integer decodeJWTId(String sign){
        DecodedJWT verify = jwtVerifier.build().verify(sign);
        return verify.getClaim("id").asInt();
    }
    public static List<String> decodeJWTIdRoles(String sign){
        DecodedJWT verify = jwtVerifier.build().verify(sign);
        return verify.getClaim("roles").asList(String.class);
    }

}
