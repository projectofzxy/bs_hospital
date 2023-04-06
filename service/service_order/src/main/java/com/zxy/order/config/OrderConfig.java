package com.zxy.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : OrderConfig  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  14:58
 */
@Configuration
@MapperScan("com.zxy.order.mapper")
public class OrderConfig {
}
