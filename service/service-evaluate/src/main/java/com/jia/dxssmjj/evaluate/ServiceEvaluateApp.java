package com.jia.dxssmjj.evaluate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.jia")
@EnableFeignClients(basePackages = "com.jia")
@EnableTransactionManagement
public class ServiceEvaluateApp {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEvaluateApp.class,args);
    }

}
