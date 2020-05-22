package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.RiskDO;
import com.ps.jury.api.objects.common.ApiResponse;
import com.ps.jury.api.objects.request.ApplyRequest;
import com.ps.jury.api.objects.response.VarResult;

public interface JudgeService {
    RiskDO getAuditByTenantIdAndApplyId(String tenantId, String applyId);
    ApiResponse<ApplyResultVO> apply(ApplyRequest request);
    void startProcess(VarResult varResult);
    AuditResultVO getAuditResult(RiskDO audit);
}
