package com.cumt.musicpay.service.impl;

import com.cumt.musicpay.domain.Consumer;
import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.dao.OrderDao;
import com.cumt.musicpay.domain.Product;
import com.cumt.musicpay.service.AliService;
import com.cumt.musicpay.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicpay.service.IProductService;
import com.cumt.musicpay.util.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.cumt.musicpay.service.impl.AliServiceImpl.TRADE_SUCCESS;
import static com.cumt.musicpay.service.impl.AliServiceImpl.WAIT_BUYER_PAY;
import static com.cumt.musicpay.util.OrderStatus.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements IOrderService {

    @Resource
    private IProductService iProductService;

    @Resource
    private AliService aliService;

    @Override
    public Order createdOrder(Integer consumerId, Integer produceId) {
        Order one = lambdaQuery().eq(Order::getUserId, consumerId)
                .eq(Order::getProductId, produceId)
                .eq(Order::getOrderStatus,NOCOST).one();
        if(ObjectUtils.isNotEmpty(one)) return one;
        Product product = iProductService.getById(produceId);
        iProductService.lambdaUpdate().eq(Product::getId,produceId).gt(Product::getId,3)
                .set(Product::getCount,product.getCount()-1).update();
        Order Order = new Order();
        Order.setTitle(product.getTitle());
        Order.setOrderNo(createdNo());
        Order.setUserId(consumerId);
        Order.setTotalFee(product.getPrice());
        Order.setOrderStatus(NOCOST);
        Order.setProductId(produceId);
        Order.setCreateTime(LocalDateTime.now());
        Order.setUpdateTime(LocalDateTime.now());
        save(Order);
        return Order;
    }

    private String createdNo(){
        String uuid= UUID.randomUUID().toString().replace("-","");
        long time=System.currentTimeMillis();
        return uuid+time;
    }



    public static BigDecimal getBigDecimal(Integer i){
        return (new BigDecimal(i)).divide(new BigDecimal("100"));
    }

    public static Integer getTotalFromBD(BigDecimal bigDecimal){
        return bigDecimal.multiply(new BigDecimal("100")).intValue();
    }

    @Override
    public void updatePaySuccess(String orderNo) {
        lambdaUpdate().eq(Order::getOrderNo,orderNo)
                .set(Order::getOrderStatus,HASCOST)
                .set(Order::getUpdateTime,LocalDateTime.now())
                .update();
    }

    @Override
    public List<Order> getCheckOrder(long sec) {

        LocalDateTime ldt=LocalDateTime.now().minusSeconds(sec);
        List<Order> list = lambdaQuery().eq(Order::getOrderStatus, NOCOST)
                .lt(Order::getCreateTime, ldt).list();
        if(ObjectUtils.isEmpty(list)) return new ArrayList<>();
        return list;
    }

    @Override
    public Result getOrderByConId() {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        List<Order> list = lambdaQuery().eq(Order::getUserId, o.getId()).orderByDesc(Order::getCreateTime).list();
        return Result.ok(list);
    }

    @Override
    public Result payOrderByOrderNo(String orderNo) {
        String s = aliService.queryOrder(orderNo);
        if(ObjectUtils.isEmpty(s)){
            Order one = lambdaQuery().eq(Order::getOrderNo, orderNo).one();
            long l = Duration.between(one.getCreateTime(), LocalDateTime.now()).toMinutes();
            if(l>=30 ) {
                lambdaUpdate().eq(Order::getOrderNo,orderNo).set(Order::getOrderStatus,ORDER_TTL).update();
                return Result.fail("订单已过期");
            }
            return aliService.Buy(one);
        }else {
            try {
                ObjectMapper objectMapper=new ObjectMapper();
                HashMap<String, HashMap<String,String>> hashMap = objectMapper.readValue(s, HashMap.class);
                HashMap<String, String> responedMap = hashMap.get("alipay_trade_query_response");
                String tradeStatus = responedMap.get("trade_status");
                if(TRADE_SUCCESS.equals(tradeStatus)){
                    aliService.addressOrder(responedMap);
                    return Result.fail("您已支付过，几分钟后刷新页面重新查看");
                }
                if(WAIT_BUYER_PAY.equals(tradeStatus)){
                    Order one = lambdaQuery().eq(Order::getOrderNo, orderNo).one();
                    return aliService.Buy(one);

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Result.fail("操作失败");
        }
    }

    @Override
    public Result closeOrderByOrderNo(String orderNo) {
        String s = aliService.queryOrder(orderNo);
        if (ObjectUtils.isEmpty(s)) {
            lambdaUpdate().eq(Order::getOrderNo, orderNo)
                    .set(Order::getOrderStatus, ORDERCANCEL)
                    .set(Order::getUpdateTime, LocalDateTime.now())
                    .update();
            return Result.ok();
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HashMap<String, HashMap<String, String>> hashMap = objectMapper.readValue(s, HashMap.class);
                HashMap<String, String> responedMap = hashMap.get("alipay_trade_query_response");
                String tradeStatus = responedMap.get("trade_status");
                if (TRADE_SUCCESS.equals(tradeStatus)) {
                    aliService.addressOrder(responedMap);
                    return Result.fail("您已支付过，无法关闭，请刷新页面");
                }
                if (WAIT_BUYER_PAY.equals(tradeStatus)) {
                    aliService.closeOrder(orderNo,ORDERCANCEL);
                    lambdaUpdate().eq(Order::getOrderNo, orderNo)
                            .set(Order::getOrderStatus, ORDERCANCEL)
                            .set(Order::getUpdateTime, LocalDateTime.now())
                            .update();
                    return Result.ok();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Result.fail("操作失败");
        }
    }

    @Override
    public Result flushOrderByOrderNo(String orderNo) {
        String s = aliService.queryOrder(orderNo);
        if (ObjectUtils.isEmpty(s)) {
            return Result.fail("您还未支付");
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                HashMap<String, HashMap<String, String>> hashMap = objectMapper.readValue(s, HashMap.class);
                HashMap<String, String> responedMap = hashMap.get("alipay_trade_query_response");
                String tradeStatus = responedMap.get("trade_status");
                if (TRADE_SUCCESS.equals(tradeStatus)) {
                    aliService.addressOrder(responedMap);
                    return Result.ok("状态更新");
                }
                if (WAIT_BUYER_PAY.equals(tradeStatus)) {
                    return Result.fail("您还未支付");
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Result.fail("操作失败");
        }
    }
}
