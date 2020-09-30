package com.ts.judge.provider.config;

import com.ts.jury.api.feign.ApiFeignFallbackFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JudgeAutoConfiguration {

    //    @Bean
//    @ConditionalOnMissingBean(RuleTemplate.class)
//    public RuleTemplate ruleTemplate() {
//        return new DroolsRuleTemplate();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(RuleContext.class)
//    public RuleContext ruleContext() {
//        return new DroolsRuleContext();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(RuleExecutor.class)
//    public RuleExecutor ruleExecutor() {
//        return new DroolsRuleExecutor();
//    }
//
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
//
//    @Bean
//    @ConditionalOnMissingBean(HttpHeaders.class)
//    public HttpHeaders httpHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return headers;
//    }
//
    @Bean
    @ConditionalOnMissingBean(ApiFeignFallbackFactory.class)
    public ApiFeignFallbackFactory apiFeignFallbackFactory() {
        return new ApiFeignFallbackFactory();
    }
}
