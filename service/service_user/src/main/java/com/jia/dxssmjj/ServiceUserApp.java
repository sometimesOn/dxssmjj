package com.jia.dxssmjj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.jia")
@ServletComponentScan
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.jia")
@EnableFeignClients(basePackages = "com.jia")
@EnableTransactionManagement
public class ServiceUserApp {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApp.class,args);
    }
    

}
