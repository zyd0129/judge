package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;

import java.util.Map;

public interface ProcessService {
    AuditTaskDO getAuditTask(int id);

    AuditTaskDO getAuditTask(String tenantCode, String applyId);

    boolean updateAuditStatus(int status, int taskId);

    ApiResponse<ApplyResultVO> apply(ApplyRequest request);

    ApiResponse<ApplyResultVO> retryAudit(AuditTaskDO auditTask);

    ApiResponse<String> saveVarResult(AuditTaskDO auditTask, Map map);

    ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO audit);

    void reapplyJury();

    void varResultQuery();

    void auditVariable();

    void callbackTenant();
}
