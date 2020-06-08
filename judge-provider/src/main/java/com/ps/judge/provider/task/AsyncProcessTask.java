package com.ps.judge.provider.task;

import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;

public interface AsyncProcessTask {
    void applyJury(int auditId, ApplyRequest request);

    void startProcess(AuditTaskDO auditTask, VarResult varResult);

    void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse);
}
