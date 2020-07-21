package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.ProcessService;
import com.ps.judge.provider.task.AsyncProcessTask;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 规则处理服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AsyncProcessTask asyncProcessTask;

    @Override
    @Transactional
    public ApiResponse<String> saveVarResult(AuditTaskDO auditTask, Map map) {
        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            Integer auditId = auditTask.getId();
            auditTask.setTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED_SUCCESS.getCode());
            this.auditTaskMapper.update(auditTask);
            String varResultString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
            this.auditTaskParamMapper.updateVarResult(varResultString, auditId, LocalDateTime.now());
            this.asyncProcessTask.startProcess(auditTask, map);
        }
        return ApiResponse.success();
    }

    @Override
    public void varResultQuery() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            ApiResponse<Map> apiResponse = this.juryApi.getVarResult(auditTask.getApplyId(), auditTask.getTenantCode());
            if (apiResponse.isSuccess()) {
                this.saveVarResult(auditTask, apiResponse.getData());
            }
        }
    }

    @Override
    public void auditVariable() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED_SUCCESS.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getId());
            Map varResultMap = JSON.parseObject(auditTaskParam.getVarResult(), Map.class);
            this.asyncProcessTask.startProcess(auditTask, varResultMap);
        }
    }
}