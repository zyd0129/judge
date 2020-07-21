package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.provider.service.AuditTaskService;
import com.ps.jury.api.common.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * AuditTask 业务订单服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/21
 */
@Service
public class AuditTaskServiceImpl implements AuditTaskService {
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;

    @Override
    public AuditTaskDO getAuditTask(int id) {
        return this.auditTaskMapper.getAuditTaskById(id);
    }

    @Override
    public AuditTaskDO getAuditTask(String tenantCode, String applyId) {
        return this.auditTaskMapper.getAuditTask(tenantCode, applyId);
    }

    @Override
    public boolean updateAuditTask(AuditTaskDO auditTask) {
        auditTask.setGmtModified(LocalDateTime.now());
        return this.auditTaskMapper.update(auditTask) > 0;
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO auditTask) {
        AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getId());
        if (StringUtils.isNotEmpty(auditTaskParam.getOutputRawParam())) {
            return JSON.parseObject(auditTaskParam.getOutputRawParam(), ApiResponse.class);
        }
        AuditResultVO auditResult = new AuditResultVO();
        BeanUtils.copyProperties(auditTask, auditResult);
        return ApiResponse.success(auditResult);
    }
}
