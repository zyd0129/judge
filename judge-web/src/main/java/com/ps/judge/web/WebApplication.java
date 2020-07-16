package com.ps.judge.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ps.judge.dao.mapper")
@EnableFeignClients(basePackages = "com.ps.judge.api")
public class WebApplication  {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
