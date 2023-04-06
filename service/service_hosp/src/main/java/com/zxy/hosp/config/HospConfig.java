package com.zxy.hosp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : HospConfig  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/1  15:54
 */
@Configuration
@MapperScan("com.zxy.hosp.mapper")
public class HospConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
