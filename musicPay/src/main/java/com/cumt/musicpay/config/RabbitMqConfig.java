package com.cumt.musicpay.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cumt.musicpay.schedule.RabbitScheTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RabbitMqConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    ObjectMapper objectMapper=new ObjectMapper();

    @Resource
    private RabbitScheTask rabbitScheTask;

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if(!b) {
            List<Map<String, String>> list = rabbitScheTask.getList();
            synchronized (list) {
                Message returnedMessage = correlationData.getReturnedMessage();
                if (ObjectUtils.isNotEmpty(returnedMessage)) {
                    String s1 = new String(returnedMessage.getBody());
                    try {
                        Map<String,String> map = objectMapper.readValue(s1, HashMap.class);
                        list.add(map);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        List<Map<String, String>> list = rabbitScheTask.getList();
        synchronized (list) {
            Map<String, String> map = new HashMap<>();
            map.put("exchange", s1);
            map.put("key", s2);
            map.put("message", new String(message.getBody()));
            list.add(map);
        }

    }
}
