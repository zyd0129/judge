package com.ps.judge.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ps.judge.dao.mappers")
public class JudgeProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(JudgeProviderApplication.class, args);
    }

}
