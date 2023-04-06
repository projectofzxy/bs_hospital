package com.zxy.msm.utils;

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
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${aliyun.sms.regionId}")
    private String regionId;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.secret}")
    private String secret;
    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id=regionId;
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
    }
}
