package com.zxy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.order.OrderInfo;
import com.zxy.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    void savePaymentInfo(OrderInfo orderInfo, Integer status);

    void paySuccess(String out_trade_no, Map<String, String> resultMap);
    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
