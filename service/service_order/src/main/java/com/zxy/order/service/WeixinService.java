package com.zxy.order.service;

import java.util.Map;

public interface WeixinService {
    Map createNative(Long orderId) throws Exception;

    Map<String, String> queryPayStatus(Long orderId);

    /***
     * 退款
     * @param orderId
     * @return
     */
    Boolean refund(Long orderId);

}
