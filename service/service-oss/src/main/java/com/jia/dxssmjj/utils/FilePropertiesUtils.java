package com.jia.dxssmjj.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePropertiesUtils implements InitializingBean {

    @Value("${aliyun.oss.endpoint}")
    public String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    public String accessKeyId;

    @Value("${aliyun.oss.secret}")
    public String secret;

    @Value("${aliyun.oss.bucketName}")
    public String bucketName;

    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String SECRET;

    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        BUCKET_NAME = bucketName;
        ENDPOINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        SECRET = secret;
    }
}
