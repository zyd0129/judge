package com.ps.judge.provider.service;

import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;

/**
 * AuditTask 业务订单服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/21
 */
public interface AuditTaskService {
    AuditTaskDO getAuditTask(int id);

    AuditTaskDO getAuditTask(String tenantCode, String applyId);

    boolean updateAuditTask(AuditTaskDO auditTask);

    ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO audit);
}
