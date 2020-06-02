package com.ps.judge.web.config;

import com.ps.judge.web.service.StorageService;
import com.ps.judge.web.service.impl.OssStorageServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnClass({StorageService.class})
@EnableConfigurationProperties({JudgeWebProperties.class})
public class JudgeWebAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    public StorageService ossStorageService() {
        return new OssStorageServiceImpl();
    }

}
