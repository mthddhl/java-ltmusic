package com.cumt.musicpay.service;

import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.util.Result;

import java.util.Map;

public interface AliService {
    Result createdTrance(Integer consumerId, Integer produceId);

    void addressOrder(Map<String, String> param);

    void closeOrder(String orderNo,String status);

    String queryOrder(String orderNo);

    void checkAndUpdate(String orderNo);

    Result Buy(Order order);
}
