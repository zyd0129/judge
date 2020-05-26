package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;

public interface JudgeService {
    AuditTaskDO getAuditByTenantIdAndApplyId(String tenantId, String applyId);
    ApiResponse<ApplyResultVO> apply(ApplyRequest request);
    void startProcess(VarResult varResult);
    AuditResultVO getAuditResult(AuditTaskDO audit);
}
