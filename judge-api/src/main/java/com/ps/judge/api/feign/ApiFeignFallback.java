package com.ps.judge.api.feign;

import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.LoadFlowVO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiFeignFallback implements JudgeApi {
    private final Throwable throwable;

    public ApiFeignFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ApiResponse<ApplyResultVO> applyAudit(ApplyRequest applyRequest) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(Integer taskId) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }

    @Override
    public ApiResponse<String> loadFlow(LoadFlowVO loadFlowVO) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }

    @Override
    public ApiResponse<String> submitVar(ApiResponse<Map> apiResponse) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditResultQuery auditResultQuery) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }
}
