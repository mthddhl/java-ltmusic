package com.cumt.musicpay.config;

import com.alipay.api.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:alipay.properties")
public class MyPayConfig {

    @Resource
    private Environment environment;

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig=new AlipayConfig();
        //设置appId
        alipayConfig.setAppId(environment.getProperty("alipay.add-id"));
        //设置商户私钥
        alipayConfig.setPrivateKey(environment.getProperty("alipay.merchant-private-key"));
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(environment.getProperty("alipay.alipay-public-key"));
        //设置支付宝网关
        alipayConfig.setServerUrl(environment.getProperty("alipay.gateway-url"));
        //设置请求格式,固定值json.
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        //设置字符集
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        //设置签名类型
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        //构造client
        AlipayClient alipayClient=new DefaultAlipayClient(alipayConfig);
        return alipayClient;
    }
    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer()
                .setAddress("redis://120.25.161.31:6379").setPassword("159753");
        return Redisson.create(config);
    }

}
