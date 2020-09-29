package com.ps.judge.provider.task;

import com.ps.judge.dao.entity.AuditTaskDO;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public interface AsyncProcessTask {
    void applyJury(AuditTaskDO auditTask, ApplyRequest request);

    void startProcess(AuditTaskDO auditTask, Map map);
}
