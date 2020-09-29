package com.ts.judge.provider.controller;

import com.ts.judge.api.JudgeApi;
import com.ts.judge.provider.flow.ProcessEngine;
import com.ts.judge.api.object.ProcessResult;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.jury.api.request.ApplyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController implements JudgeApi {
    @Autowired
    ProcessEngine processEngine;

    @Override
    public ApiResponse<ProcessResult> applyAudit(@Validated ApplyRequest applyRequest) {
        ProcessResult processResult = processEngine.execute(applyRequest.getFlowCode(), applyRequest);
        return ApiResponse.success(processResult);
    }
}