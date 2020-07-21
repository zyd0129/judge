package com.ps.judge.provider.config;

import com.ps.judge.provider.handler.RestResponseErrorHandler;
import com.ps.judge.provider.rule.builder.DroolsRuleTemplate;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.manager.DroolsRuleManager;
import com.ps.judge.provider.rule.manager.RuleManager;
import com.ps.jury.api.feign.ApiFeignFallbackFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(JudgeProviderProperties.class)
public class JudgeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RuleTemplate.class)
    public RuleTemplate ruleTemplate() {
        return new DroolsRuleTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(RuleManager.class)
    public RuleManager ruleManager() {
        return new DroolsRuleManager();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(HttpHeaders.class)
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Bean
    @ConditionalOnMissingBean(ApiFeignFallbackFactory.class)
    public ApiFeignFallbackFactory apiFeignFallbackFactory() {
        return new ApiFeignFallbackFactory();
    }
}
