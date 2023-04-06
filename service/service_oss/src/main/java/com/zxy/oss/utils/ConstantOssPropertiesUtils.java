package com.zxy.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ConstantPropertiesUtils  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/14  16:00
 */
@Component
public class ConstantOssPropertiesUtils implements InitializingBean {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.secret}")
    private String secret;

    @Value("${aliyun.oss.bucket}")
    private String bucket;
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    public static String BUCKET;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT=endpoint;
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
        BUCKET=bucket;
    }
}
