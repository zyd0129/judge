package com.ts.judge.api.feign;

import com.ts.judge.api.JudgeApi;
import com.ts.judge.api.object.ProcessResult;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.jury.api.request.ApplyRequest;
import org.springframework.http.HttpStatus;

public class ApiFeignFallback implements JudgeApi {
    private final Throwable throwable;

    public ApiFeignFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ApiResponse<ProcessResult> applyAudit(ApplyRequest applyRequest) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.throwable.getMessage());
    }
}
