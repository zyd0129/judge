package com.ps.judge.api;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.LoadFlowVO;
import com.ps.judge.api.feign.ApiFeignFallbackFactory;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "judge-provider", fallbackFactory = ApiFeignFallbackFactory.class)
public interface JudgeApi {
    @PostMapping("/judge/audit/apply")
    ApiResponse<ApplyResultVO> applyAudit(@RequestBody ApplyRequest applyRequest);

    @PostMapping("/judge/audit/retry")
    ApiResponse<ApplyResultVO> retryAudit(Integer taskId);

    @PostMapping("/judge/audit/flow/load")
    ApiResponse<String> loadFlow(@RequestBody LoadFlowVO loadFlowVO);

    @PostMapping("/judge/var/submit")
    ApiResponse<String> submitVar(@RequestBody ApiResponse<Map> apiResponse);

    @PostMapping("/judge/audit/result")
    ApiResponse<AuditResultVO> getAuditResult(@RequestBody AuditResultQuery auditResultQuery);
}
