package com.zxy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName : ServiceUserApplication  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/13  22:27
 */

@SpringBootApplication
@ComponentScan(basePackages = "com.zxy")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zxy")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}

