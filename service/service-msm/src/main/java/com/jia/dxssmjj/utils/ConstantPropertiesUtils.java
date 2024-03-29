package com.jia.dxssmjj.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${aliyun.sms.regionId}")
    public String regionId;

    @Value("${aliyun.sms.accessKeyId}")
    public String accessKeyId;

    @Value("${aliyun.sms.secret}")
    public String secret;

    public static String REGION_ID;
    public static String ACCESS_KEY_ID;
    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_ID = regionId;
        ACCESS_KEY_ID = accessKeyId;
        SECRET = secret;
    }
}
