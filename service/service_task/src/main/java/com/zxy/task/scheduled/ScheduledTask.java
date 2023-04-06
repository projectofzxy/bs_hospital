package com.zxy.task.scheduled;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zxy.common.rabbit.constant.MqConst;
import com.zxy.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ScheduledTask  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/23  18:57
 */
@Component
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    /**
     * 每天8点执行 提醒就诊
     */

//    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/30 * * * * ?")
    @XxlJob("yyghJobHandler")
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }
}
