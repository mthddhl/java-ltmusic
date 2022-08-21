package com.cumt.musicpay.service;

import com.cumt.musicpay.domain.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-08-07
 */
public interface IPaymentInfoService extends IService<PaymentInfo> {

    void createdPaymentInfo(Map<String, String> param);
}
