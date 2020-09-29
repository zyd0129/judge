package com.ts.judge.api;


import com.ts.judge.api.feign.ApiFeignFallbackFactory;
import com.ts.judge.api.object.ProcessResult;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.jury.api.request.ApplyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "judge-provider", fallbackFactory = ApiFeignFallbackFactory.class)
public interface JudgeApi {
    @PostMapping("/judge/audit/apply")
    ApiResponse<ProcessResult> applyAudit(@RequestBody ApplyRequest applyRequest);
}
