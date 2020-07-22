package com.ps.judge.provider.service;

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
    ApiResponse<String> saveVarResult(AuditTaskDO auditTask, Map map);

    void varResultQuery();

    void auditVariable();
}
