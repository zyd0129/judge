package com.ps.judge.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JudgeProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(JudgeProviderApplication.class, args);
    }

}
