package com.cumt.musicserver.schedule;



import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import static com.cumt.musicserver.util.StaticString.CONSUMER_REDIS_ID;

@Component
public class SecheduleTask {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(fixedDelay = (2*60*1000))
    public void task1(){
        stringRedisTemplate.hasKey(CONSUMER_REDIS_ID + "1");
    }
}
