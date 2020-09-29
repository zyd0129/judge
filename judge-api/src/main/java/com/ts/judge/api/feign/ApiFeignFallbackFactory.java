package com.ts.judge.api.feign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ApiFeignFallbackFactory implements FallbackFactory<ApiFeignFallback> {
    @Override
    public ApiFeignFallback create(Throwable throwable) {
        return new ApiFeignFallback(throwable);
    }
}
