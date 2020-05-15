package com.ps.judge.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient
public interface JudgeApi {
    @GetMapping("/judge/")
    public Object apply();
}
