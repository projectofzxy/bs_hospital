package com.zxy.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.order.OrderInfo;
import com.zxy.yygh.vo.order.OrderCountQueryVo;
import com.zxy.yygh.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrder(String orderId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    Map<String,Object> show(Long id);

    Boolean cancelOrder(Long orderId);

    void patientTips();

    /**
     * 订单统计
     */
    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);

}
