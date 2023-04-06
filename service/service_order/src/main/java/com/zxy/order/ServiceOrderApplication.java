package com.zxy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName : ServiceOrderApplication  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/21  22:54
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.zxy"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zxy"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
