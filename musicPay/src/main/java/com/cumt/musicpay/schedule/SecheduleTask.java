package com.cumt.musicpay.schedule;


import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.service.AliService;
import com.cumt.musicpay.service.IOrderService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.cumt.musicpay.filter.SpringSecurityFilter.CONSUMER_REDIS_ID;

@Component
public class SecheduleTask {

    @Resource
    private IOrderService iOrderService;

    @Resource
    private AliService aliService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(fixedDelay = (2*60*1000))
    public void task(){

        List<Order> list=iOrderService.getCheckOrder(60*30);

        for (Order e:list){
            aliService.checkAndUpdate(e.getOrderNo());
        }
    }
    @Scheduled(fixedDelay = (2*60*1000))
    public void task1(){
        System.out.println(1);
        stringRedisTemplate.hasKey(CONSUMER_REDIS_ID + "1");
    }
}