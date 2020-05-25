package com.ps.judge.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ps.judge.dao.mapper")
@EnableFeignClients(basePackages = "com.ps.jury.api")
public class JudgeProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(JudgeProviderApplication.class, args);
    }

}
