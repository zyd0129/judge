package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;

public interface ProcessService {
    AuditTaskDO getAuditTask(String tenantCode, String applyId);

    ApiResponse<ApplyResultVO> apply(ApplyRequest request);

    ApiResponse saveVarResult(AuditTaskDO auditTask, VarResult varResult);

    ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO audit);

    void reapplyJury();

    void varResultQuery();

    void auditVariable();

    void callbackTenant();

    void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse);
}
