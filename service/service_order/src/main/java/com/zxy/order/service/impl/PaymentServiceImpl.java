package com.zxy.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.common.exception.YyghException;
import com.zxy.common.helper.HttpRequestHelper;
import com.zxy.common.result.ResultCodeEnum;
import com.zxy.hosp.client.HospitalFeignClient;
import com.zxy.order.mapper.PaymentMapper;
import com.zxy.order.service.OrderService;
import com.zxy.order.service.PaymentService;
import com.zxy.yygh.enums.OrderStatusEnum;
import com.zxy.yygh.enums.PaymentStatusEnum;
import com.zxy.yygh.enums.PaymentTypeEnum;
import com.zxy.yygh.model.order.OrderInfo;
import com.zxy.yygh.model.order.PaymentInfo;
import com.zxy.yygh.vo.order.SignInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : PaymentServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  21:45
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper,PaymentInfo> implements PaymentService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HospitalFeignClient hospitalFeignClient;
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderInfo.getId());
        wrapper.eq("payment_type",paymentType);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            return;
        }
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")+"|"+orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        baseMapper.insert(paymentInfo);
    }

    @Override
    public void paySuccess(String out_trade_no, Map<String, String> resultMap) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no",out_trade_no);
        wrapper.eq("payment_type", PaymentTypeEnum.WEIXIN.getStatus());
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);


        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfo);

        OrderInfo orderInfo = orderService.getById(paymentInfo.getOrderId());
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderService.updateById(orderInfo);

        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        Map<String,Object> reqMap=new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl() + "/order/updatePayStatus");
        if(result.getInteger("code") != 200) {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
    }

    @Override
    public PaymentInfo getPaymentInfo(Long orderId, Integer paymentType) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("payment_type", paymentType);
        return baseMapper.selectOne(queryWrapper);
    }
}
