package com.ps.judge.provider.service;

import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.jury.api.common.ApiResponse;

/**
 * 商户回调
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
public interface CallbackService {

    void sendAuditTaskResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse);

    void callbackTenant();
}
