package com.zxy.hosp.receive;

import com.rabbitmq.client.Channel;
import com.zxy.common.rabbit.constant.MqConst;
import com.zxy.common.rabbit.service.RabbitService;
import com.zxy.hosp.sercice.ScheduleService;
import com.zxy.yygh.model.hosp.Schedule;
import com.zxy.yygh.vo.msm.MsmVo;
import com.zxy.yygh.vo.order.OrderMqVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName : HospitalReceiver  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  16:45
 */
@Component
public class HospitalReceiver {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RabbitService rabbitService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) {
        //下单成功更新预约数
        if(orderMqVo.getScheduleId()==null){
            return;
        }
        if (null!=orderMqVo.getAvailableNumber()) {
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            scheduleService.update(schedule);
        }else {
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            int availableNumber = schedule.getAvailableNumber() + 1;
            schedule.setAvailableNumber(availableNumber);
            scheduleService.update(schedule);
        }
        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

}
