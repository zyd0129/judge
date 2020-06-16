package com.ps.judge.web.config;

import com.ps.judge.web.service.StorageService;
import com.ps.judge.web.service.impl.OssStorageServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass({StorageService.class})
@EnableConfigurationProperties({JudgeWebProperties.class})
public class JudgeWebAutoConfiguration  implements WebMvcConfigurer {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    public StorageService ossStorageService() {
        return new OssStorageServiceImpl();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
