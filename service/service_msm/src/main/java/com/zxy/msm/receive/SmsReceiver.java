package com.zxy.msm.receive;

import com.rabbitmq.client.Channel;
import com.zxy.common.rabbit.constant.MqConst;
import com.zxy.msm.service.MsmService;
import com.zxy.yygh.vo.msm.MsmVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName : SmsReceiver  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  16:34
 */
@Component
public class SmsReceiver {
    @Autowired
    private MsmService msmService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_MSM_ITEM,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_MSM),
            key = {MqConst.ROUTING_MSM_ITEM}
    ))
    public void send(MsmVo msmVo, Message message, Channel channel){
        msmService.send(msmVo);

    }
}
