package com.zxy.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName : ServiceHospApplication  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/1  15:23
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.zxy")
@EnableDiscoveryClient//开启nacos服务发现
@EnableFeignClients(basePackages = "com.zxy")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }

}
