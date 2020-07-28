package com.ps.judge.provider.config;

import com.ps.judge.provider.handler.RestResponseErrorHandler;
import com.ps.judge.provider.flow.rule.builder.DroolsRuleTemplate;
import com.ps.judge.provider.flow.rule.builder.RuleTemplate;
import com.ps.judge.provider.flow.rule.context.DroolsRuleContext;
import com.ps.judge.provider.flow.rule.context.RuleContext;
import com.ps.judge.provider.flow.rule.executor.DroolsRuleExecutor;
import com.ps.judge.provider.flow.rule.executor.RuleExecutor;
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
    @ConditionalOnMissingBean(RuleContext.class)
    public RuleContext ruleContext() {
        return new DroolsRuleContext();
    }

    @Bean
    @ConditionalOnMissingBean(RuleExecutor.class)
    public RuleExecutor ruleExecutor() {
        return new DroolsRuleExecutor();
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
