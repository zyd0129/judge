package com.ps.judge.api;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.feign.ApiFeignFallbackFactory;
import com.ps.jury.api.objects.common.ApiResponse;
import com.ps.jury.api.objects.request.ApplyRequest;
import com.ps.jury.api.objects.response.VarResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "judge-provider", fallbackFactory = ApiFeignFallbackFactory.class)
public interface JudgeApi {
    @PostMapping("/judge/audit/apply")
    ApiResponse<ApplyResultVO> applyAudit(@RequestBody ApplyRequest applyRequest);

    @PostMapping("/judge/var/submit")
    ApiResponse<String> submitVar(@RequestBody ApiResponse<VarResult> apiResponse);

    @PostMapping("/judge/audit/result")
    ApiResponse<AuditResultVO> getAuditResult(@RequestBody AuditResultQuery auditResultQuery);
}
