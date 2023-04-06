package com.zxy.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.order.mapper.RefundInfoMapper;
import com.zxy.order.service.RefundInfoService;
import com.zxy.yygh.enums.RefundStatusEnum;
import com.zxy.yygh.model.order.PaymentInfo;
import com.zxy.yygh.model.order.RefundInfo;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName : RefundInfoServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/23  16:50
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {
    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        QueryWrapper<RefundInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",paymentInfo.getOrderId());
        wrapper.eq("payment_type",paymentInfo.getPaymentType());
        RefundInfo refundInfo = baseMapper.selectOne(wrapper);
        if (refundInfo!=null){
            return refundInfo;
        }
        // 保存交易记录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
//paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        baseMapper.insert(refundInfo);

        return refundInfo;
    }
}
