package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface ProcessService {
    AuditTaskDO getAuditTask(int id);

    AuditTaskDO getAuditTask(String tenantCode, String applyId);

    ApiResponse<ApplyResultVO> apply(ApplyRequest request);

    ApiResponse<ApplyResultVO> retryAudit(AuditTaskDO auditTask);

    ApiResponse<String> saveVarResult(AuditTaskDO auditTask, VarResult varResult);

    ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO audit);

    boolean updateAuditStatus(int taskId, int status);

    boolean loadFlow(ConfigFlowDO configFlow);

    void reapplyJury();

    void varResultQuery();

    void auditVariable();

    void callbackTenant();

    void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse);
}
