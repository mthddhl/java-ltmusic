package com.cumt.musicpay.service;

import com.cumt.musicpay.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicpay.util.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
public interface IOrderService extends IService<Order> {

    Order createdOrder(Integer consumerId, Integer produceId);


    void updatePaySuccess(String orderNo);

    List<Order> getCheckOrder(long sec);

    Result getOrderByConId();

    Result payOrderByOrderNo(String orderNo);

    Result closeOrderByOrderNo(String orderNo);

    Result flushOrderByOrderNo(String orderNo);
}
