package com.cumt.musicpay.controller;

import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.cumt.musicpay.domain.Consumer;
import com.cumt.musicpay.domain.Order;
import com.cumt.musicpay.service.AliService;
import com.cumt.musicpay.service.IOrderService;
import com.cumt.musicpay.service.impl.OrderServiceImpl;
import com.cumt.musicpay.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static com.cumt.musicpay.util.OrderStatus.ORDERCANCEL;

@RestController
@RequestMapping("/aliPayApi")
public class AliPayController {

    @Resource
    private AliService aliService;

    @Resource
    private Environment environment;

    @Resource
    private IOrderService iOrderService;

    @RequestMapping("/createdOrder")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result createdOrder(Integer produceId){
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        return aliService.createdTrance(o.getId(),produceId);
    }

    @RequestMapping("/notify")
    public String notify(@RequestParam Map<String,String> param){
        for (Map.Entry<String,String> s: param.entrySet()){
            System.out.println(s.getKey()+"==================="+s.getValue());
        }
        String s="failure";
        try{
            boolean b = AlipaySignature.rsaCheckV1(
                    param,
                    environment.getProperty("alipay.alipay-public-key"),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2);
            if(!b){
                System.out.println("异步验签失败");
                return s;
            }
            System.out.println("异步验签成功");
            Order out_trade_no = iOrderService.lambdaQuery()
                    .eq(Order::getOrderNo, param.get("out_trade_no")).one();
            if(ObjectUtils.isEmpty(out_trade_no)){
                System.out.println("订单不存在");
                return s;
            }

            String seller_id = param.get("seller_id");
            if(!seller_id.equals(environment.getProperty("alipay.seller-id"))){
                System.out.println("商户Pid错误");
                return s;
            }

            String total_amount = param.get("total_amount");
            BigDecimal bigDecimal = new BigDecimal(total_amount);
            BigDecimal bigDecimal1 = OrderServiceImpl.getBigDecimal(out_trade_no.getTotalFee());
            int i = bigDecimal.compareTo(bigDecimal1);
            if((new BigDecimal(total_amount)
                    .compareTo(OrderServiceImpl.getBigDecimal(out_trade_no.getTotalFee())))!=0){
                System.out.println("金额验证失败");
                return s;
            }
            String app_id = param.get("app_id");
            if(environment.containsProperty("alipay.add-id") && !Objects.equals(environment.getProperty("alipay.add-id"), app_id)){
                System.out.println("商家id验证失败");
                return s;
            }

            String trade_status = param.get("trade_status");

            if(!"TRADE_SUCCESS".equals(trade_status)){
                System.out.println("交易未成功");
                return s;
            }

            aliService.addressOrder(param);

            s="success";
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    @RequestMapping("/closeOrder")
    public void closeOrder(String orderNo){

        aliService.closeOrder(orderNo,ORDERCANCEL);
    }

    @RequestMapping("/queryOrder")
    public String queryOrder(String orderNo){
        return aliService.queryOrder(orderNo);
    }


}
