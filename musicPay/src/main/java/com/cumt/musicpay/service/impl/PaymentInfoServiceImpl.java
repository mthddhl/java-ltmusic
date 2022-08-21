package com.cumt.musicpay.service.impl;

import com.cumt.musicpay.domain.PaymentInfo;
import com.cumt.musicpay.dao.PaymentInfoDao;
import com.cumt.musicpay.service.IPaymentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static com.cumt.musicpay.util.OrderStatus.HASCOST;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoDao, PaymentInfo> implements IPaymentInfoService {

    @Override
    public void createdPaymentInfo(Map<String, String> param) {
        PaymentInfo PaymentInfo=new PaymentInfo();
        PaymentInfo.setOrderNo(param.get("out_trade_no"));
        PaymentInfo.setTransactionId(param.get("trade_no"));
        PaymentInfo.setPaymentType("支付宝");
        PaymentInfo.setTradeType("电脑支付");
        PaymentInfo.setTradeState(HASCOST);
        String total_amount = param.get("total_amount");
        Integer totalFromBD = OrderServiceImpl.getTotalFromBD(new BigDecimal(total_amount));
        PaymentInfo.setPayerTotal(totalFromBD);
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(PaymentInfo);
            PaymentInfo.setContent(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        PaymentInfo.setCreateTime(LocalDateTime.now());
        PaymentInfo.setUpdateTime(LocalDateTime.now());

        save(PaymentInfo);
    }
}
