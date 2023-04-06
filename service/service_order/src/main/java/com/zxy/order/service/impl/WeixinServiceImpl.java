package com.zxy.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.zxy.order.service.OrderService;
import com.zxy.order.service.PaymentService;
import com.zxy.order.service.RefundInfoService;
import com.zxy.order.service.WeixinService;
import com.zxy.order.util.ConstantPropertiesUtils;
import com.zxy.order.util.HttpClient;
import com.zxy.yygh.enums.PaymentStatusEnum;
import com.zxy.yygh.enums.PaymentTypeEnum;
import com.zxy.yygh.enums.RefundStatusEnum;
import com.zxy.yygh.model.order.OrderInfo;
import com.zxy.yygh.model.order.PaymentInfo;
import com.zxy.yygh.model.order.RefundInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName : WeixinServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  21:36
 */
@Service
public class WeixinServiceImpl implements WeixinService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RefundInfoService refundInfoService;

    @Override
    public Map createNative(Long orderId)  {
        Map paymap = (Map) redisTemplate.opsForValue().get(orderId.toString());
        if (paymap != null) {
            return paymap;
        }
        OrderInfo orderInfo = orderService.getById(orderId);
        paymentService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN.getStatus());
        //1、设置参数
        Map paramMap = new HashMap();
        paramMap.put("appid", ConstantPropertiesUtils.APPID);
        paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        String body = orderInfo.getReserveDate() + "就诊" + orderInfo.getDepname();
        paramMap.put("body", body);
        paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
        //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
        paramMap.put("total_fee", "1");
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
        paramMap.put("trade_type", "NATIVE");
        //2、HTTPClient来根据URL访问第三方接口并且传递参数
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        try {
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));

        client.setHttps(true);
        client.post();
        //3、返回第三方的数据
        String xml = client.getContent();
        Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
        Map map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("totalFee", orderInfo.getAmount());
        map.put("resultCode", resultMap.get("result_code"));
        map.put("codeUrl", resultMap.get("code_url"));//二维码地址

        if (null != resultMap.get("result_code")) {
            redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
        }

        return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap();
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        try {
            OrderInfo orderInfo = orderService.getById(orderId);
            //1、封装参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("支付状态resultMap"+resultMap);
            return resultMap;
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public Boolean refund(Long orderId) {
        try {
            PaymentInfo paymentInfo = paymentService.getPaymentInfo(orderId, PaymentTypeEnum.WEIXIN.getStatus());
            RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);
            if(refundInfo.getRefundStatus().intValue() == RefundStatusEnum.REFUND.getStatus().intValue()) {
                return true;
            }
            Map<String,String> paramMap = new HashMap<>(8);
            paramMap.put("appid",ConstantPropertiesUtils.APPID);       //公众账号ID
            paramMap.put("mch_id",ConstantPropertiesUtils.PARTNER);   //商户编号
            paramMap.put("nonce_str",WXPayUtil.generateNonceStr());
            paramMap.put("transaction_id",paymentInfo.getTradeNo()); //微信订单号
            paramMap.put("out_trade_no",paymentInfo.getOutTradeNo()); //商户订单编号
            paramMap.put("out_refund_no","tk"+paymentInfo.getOutTradeNo()); //商户退款单号
//       paramMap.put("total_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
//       paramMap.put("refund_fee",paymentInfoQuery.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("total_fee","1");
            paramMap.put("refund_fee","1");
            String paramXml = WXPayUtil.generateSignedXml(paramMap,ConstantPropertiesUtils.PARTNERKEY);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/secapi/pay/refund");
            client.setXmlParam(paramXml);
            client.setHttps(true);
            client.setCert(true);
            client.setCertPassword(ConstantPropertiesUtils.PARTNER);
            client.post();
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            if (null != resultMap && WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))) {
                refundInfo.setCallbackTime(new Date());
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
                refundInfoService.updateById(refundInfo);
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
