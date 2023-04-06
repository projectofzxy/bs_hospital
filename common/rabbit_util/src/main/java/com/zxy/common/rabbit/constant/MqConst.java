package com.zxy.common.rabbit.constant;

/**
 * @ClassName : MqConst  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  16:27
 */
public class MqConst {
    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER
            = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER  = "queue.order";
    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.msm";
    public static final String ROUTING_MSM_ITEM = "msm.item";
    //队列
    public static final String QUEUE_MSM_ITEM  = "queue.msm.item";

    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_8 = "task.8";
    //队列
    public static final String QUEUE_TASK_8 = "queue.task.8";

}

