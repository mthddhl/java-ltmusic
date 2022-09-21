package com.cumt.musicpay.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RabbitScheTask {

    @Resource
    public RabbitTemplate rabbitTemplate;

    private static final ArrayList<Map<String,String>> list=new ArrayList<>();
    ObjectMapper objectMapper=new ObjectMapper();

    @Scheduled(fixedDelay = (5*60*1000))
    public void task() throws JsonProcessingException {
        synchronized (list){
            if(!list.isEmpty()){
                for (Map<String,String> map:list){
                    String exchange = map.get("exchange");
                    String key = map.get("key");
                    HashMap<String,String> map2=objectMapper.readValue(  map.get("message"),HashMap.class);
                    CorrelationData c1=new CorrelationData();
                    Map<String,String> map1= new HashMap<>();
                    map1.put("exchange",exchange);
                    map1.put("key",key);
                    map1.put("message",objectMapper.writeValueAsString(map2));
                    byte[] bytes = objectMapper.writeValueAsBytes(map1);
                    Message m= MessageBuilder.withBody(bytes).build();
                    c1.setReturnedMessage(m);
                    rabbitTemplate.convertAndSend(exchange,key,map2,c1);
                }
            }
            list.clear();
        }
    }

    public List<Map<String,String>> getList(){
        return list;
    }
}
