package com.ps.judge.provider.service;

import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;

/**
 * 用户申请阶段服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
public interface ApplyService {
    ApiResponse<ApplyResultVO> apply(ApplyRequest request);

    ApiResponse<ApplyResultVO> retryAudit(AuditTaskDO auditTask);

    void reapplyJury();
}
