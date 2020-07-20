package com.ps.judge.provider.service;

import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;

import java.util.Map;

/**
 * 规则处理服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
public interface ProcessService {
    AuditTaskDO getAuditTask(int id);

    AuditTaskDO getAuditTask(String tenantCode, String applyId);

    boolean updateAuditStatus(int status, int taskId);

    ApiResponse<String> saveVarResult(AuditTaskDO auditTask, Map map);

    ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO audit);

    void varResultQuery();

    void auditVariable();
}
