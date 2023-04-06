package com.zxy.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : UserConfig  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/13  22:37
 */
@Configuration
@MapperScan("com.zxy.user.mapper")
public class UserConfig {
}
