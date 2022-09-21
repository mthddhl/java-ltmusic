package com.cumt.musicpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.service.AliService;
import com.cumt.musicpay.service.IOrderService;
import com.cumt.musicpay.service.IPaymentInfoService;
import com.cumt.musicpay.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.cumt.musicpay.util.OrderStatus.*;


@Service
public class AliServiceImpl implements AliService {

    @Resource
    private IOrderService iOrderService;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private Environment environment;

    @Resource
    private IPaymentInfoService iPaymentInfoService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final DefaultRedisScript<Long> VIP_BUY_RESULT;

    @Resource
    private RedissonClient redissonClient;


    static {
        VIP_BUY_RESULT=new DefaultRedisScript<>();
        VIP_BUY_RESULT.setLocation(new ClassPathResource("VIPSeckill.lua"));
        VIP_BUY_RESULT.setResultType(Long.class);
    }

    @Override
    @Transactional
    public Result createdTrance(Integer consumerId, Integer produceId) {

        Order one = iOrderService.lambdaQuery().eq(Order::getUserId, consumerId)
                .eq(Order::getProductId, produceId).eq(Order::getOrderStatus,NOCOST).one();
        if(ObjectUtils.isNotEmpty(one))return Buy(one);
        if(produceId>3){
            Long execute = stringRedisTemplate.execute(
                    VIP_BUY_RESULT,
                    Collections.emptyList(),
                    produceId.toString(), consumerId.toString()
            );
            if(execute==1){
                return Result.fail("库存不足");
            }
            if(execute==2){
                return Result.fail("请勿重复下单");
            }
        }

        Order orderInfo=iOrderService.createdOrder(consumerId,produceId);
        return Buy(orderInfo);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addressOrder(Map<String, String> param) {

        RLock lock = redissonClient
                .getLock("lock:VIP:buyAddress:" + param.get("out_trade_no"));
        if(lock.tryLock()) {
            try {
                String orderNo = param.get("out_trade_no");
                Order one = iOrderService.lambdaQuery()
                        .eq(Order::getOrderNo, orderNo).one();
                if (HASCOST.equals(one.getOrderStatus())) {
                    return;
                }
                Map<String,String> map=new HashMap<>();
                map.put("consumerId",one.getUserId().toString());
                map.put("productType", one.getTitle());
                map.put("orderNo",orderNo);
                CorrelationData c1=new CorrelationData();
                ObjectMapper objectMapper=new ObjectMapper();
                Map<String,String> map1=new HashMap<>();
                map1.put("exchange","consumer");
                map1.put("key","consumer.VIP");
                map1.put("message",objectMapper.writeValueAsString(map));
                byte[] bytes = objectMapper.writeValueAsBytes(map1);
                Message m= MessageBuilder.withBody(bytes).build();
                c1.setReturnedMessage(m);
                rabbitTemplate.convertAndSend("consumer",
                        "consumer.VIP", map,c1
                        );
//                rabbitTemplate.convertAndSend("consumer",
//                        "consumer.VIP", map);
                iOrderService.updatePaySuccess(orderNo);
                iPaymentInfoService.createdPaymentInfo(param);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void closeOrder(String orderNo,String status) {

        try {
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("out_trade_no", orderNo);
            request.setBizContent(jsonObject.toString());
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                iOrderService.lambdaUpdate().eq(Order::getOrderNo,orderNo)
                        .set(Order::getOrderStatus,ORDERCANCEL)
                        .set(Order::getUpdateTime, LocalDateTime.now())
                        .update();
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        }catch (AlipayApiException e){
            System.out.println("调用失败");
            e.printStackTrace();
        }

    }

    @Override
    public String queryOrder(String orderNo) {
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("out_trade_no", orderNo);
            request.setBizContent(jsonObject.toString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
                System.out.println(response.getBody());
                return response.getBody();
            } else {
                System.out.println("调用失败");
                return "";
            }
        }catch (AlipayApiException e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void checkAndUpdate(String orderNo) {
        String s = this.queryOrder(orderNo);
        if(ObjectUtils.isEmpty(s)){
            System.out.println("订单未创建");
            iOrderService.lambdaUpdate().eq(Order::getOrderNo,orderNo)
                    .set(Order::getOrderStatus,ORDER_TTL).update();
            return;
        }
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            HashMap<String, HashMap<String,String>> hashMap = objectMapper.readValue(s, HashMap.class);
            HashMap<String, String> responedMap = hashMap.get("alipay_trade_query_response");
            String tradeStatus = responedMap.get("trade_status");
            if(TRADE_SUCCESS.equals(tradeStatus)){
                this.addressOrder(responedMap);
            }
            if(WAIT_BUYER_PAY.equals(tradeStatus)){
                this.closeOrder(orderNo,ORDER_TTL);

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result Buy(Order orderInfo) {
        AlipayTradePagePayRequest request=new AlipayTradePagePayRequest();
        //数据
        AlipayTradePagePayModel bizModel=new AlipayTradePagePayModel();
        bizModel.setOutTradeNo(orderInfo.getOrderNo());
        //单位是元
        bizModel.setTotalAmount(OrderServiceImpl.getBigDecimal(orderInfo.getTotalFee()).toPlainString());
        bizModel.setSubject(orderInfo.getTitle());
        //默认的
        bizModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(bizModel);
//        System.out.println(environment.getProperty("alipay.return-url"));

        request.setReturnUrl(environment.getProperty("alipay.return-url"));

        request.setNotifyUrl(environment.getProperty("alipay.notify-url"));
//        //用户支付后支付宝会以GET方法请求returnUrl,并且携带out_trade_no,trade_no,total_amount等参数.
//
//        request.setReturnUrl(returnUrl);
        AlipayTradePagePayResponse response=null;
        try{
            //完成签名并执行请求
            response=alipayClient.pageExecute(request);
            if(response.isSuccess()){
                return Result.ok(response.getBody());
            }
            else{
                System.out.println(response.getMsg());
                return Result.fail("响应失败");
            }
        }
        catch(AlipayApiException e){
            return Result.fail("响应失败");
        }
    }

    public final static String TRADE_SUCCESS="TRADE_SUCCESS";
    public final static String WAIT_BUYER_PAY="WAIT_BUYER_PAY";
}
