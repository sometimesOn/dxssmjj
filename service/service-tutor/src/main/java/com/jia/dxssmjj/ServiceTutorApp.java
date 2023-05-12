package com.jia.dxssmjj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.jia")
@EnableFeignClients(basePackages = "com.jia")
@Transactional
public class ServiceTutorApp {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTutorApp.class,args);
    }
}
