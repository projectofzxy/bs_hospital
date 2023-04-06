package com.zxy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.order.PaymentInfo;
import com.zxy.yygh.model.order.RefundInfo;

/**
 * @ClassName : RefundInfoService  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/23  16:50
 */
public interface RefundInfoService extends IService<RefundInfo> {
    /**
     * 保存退款记录
     * @param paymentInfo
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
