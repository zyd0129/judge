package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.ProcessService;
import com.ps.judge.provider.task.AsyncProcessTask;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {
    private static final int MAX_RETRY_COUNT = 5;

    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AuditTaskTriggeredRuleMapper auditTaskTriggeredRuleMapper;
    @Autowired
    AsyncProcessTask asyncProcessTask;

    @Override
    public AuditTaskDO getAuditTask(int id) {
        return this.auditTaskMapper.getAuditTaskById(id);
    }

    @Override
    public AuditTaskDO getAuditTask(String tenantCode, String applyId) {
        return this.auditTaskMapper.getAuditTask(tenantCode, applyId);
    }

    @Override
    public boolean updateAuditStatus(int status, int taskId) {
        return this.auditTaskMapper.updateTaskStatus(status, taskId, LocalDateTime.now()) > 0;
    }

    @Override
    @Transactional
    public ApiResponse<ApplyResultVO> apply(ApplyRequest request) {
        AuditTaskDO auditTask = new AuditTaskDO();
        auditTask.setApplyId(request.getApplyId());
        auditTask.setFlowCode(request.getFlowCode());
        auditTask.setTenantCode(request.getTenantCode());
        auditTask.setProductCode(request.getProductCode());
        auditTask.setUserId(request.getUserId());
        auditTask.setUserName(request.getUserName());
        auditTask.setMobile(request.getMobile());
        auditTask.setIdCard(request.getIdCard());
        auditTask.setOrderId(request.getOrderId());
        auditTask.setIp(request.getIp());
        auditTask.setDeviceFingerPrint(request.getDeviceFingerPrint());
        auditTask.setTransactionTime(request.getTransactionTime());
        auditTask.setTaskStatus(AuditTaskStatusEnum.REQUEST_RECEIVED.getCode());
        auditTask.setCallbackUrl(request.getCallbackUrl());
        auditTask.setGmtCreate(LocalDateTime.now());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.insert(auditTask);

        String requestString = JSON.toJSONString(request, SerializerFeature.WriteMapNullValue);
        Integer auditId = auditTask.getId();
        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getTenantCode());
        auditTaskParam.setTaskId(auditId);
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(requestString);
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setVarResult(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);

        this.asyncProcessTask.applyJury(auditId, request);
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(AuditTaskDO auditTask) {
        int auditId = auditTask.getId();

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            if (auditTask.getRetryCount() < MAX_RETRY_COUNT) {
                return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单自动重试中，不能手动重试");
            }
        }

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.VAR_COMPUTE_FAIL.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            ApplyRequest request = JSON.parseObject(auditTaskParam.getInputRawParam(), ApplyRequest.class);
            this.asyncProcessTask.applyJury(auditId, request);
        }

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode()) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            VarResult varResult = JSON.parseObject(auditTaskParam.getVarResult(), VarResult.class);
            this.asyncProcessTask.startProcess(auditTask, varResult);
        }

        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(auditTask.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    @Transactional
    public ApiResponse<String> saveVarResult(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            Integer auditId = auditTask.getId();
            auditTask.setTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED_SUCCESS.getCode());
            this.updateAuditStatus(auditTask.getTaskStatus(), auditId);
            String varResultString = JSON.toJSONString(varResult, SerializerFeature.WriteMapNullValue);
            this.auditTaskParamMapper.updateVarResult(varResultString, auditId, LocalDateTime.now());
            this.asyncProcessTask.startProcess(auditTask, varResult);
        }
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO auditTask) {
        AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getId());
        if (StringUtils.isNotEmpty(auditTaskParam.getOutputRawParam())) {
            return JSON.parseObject(auditTaskParam.getOutputRawParam(), ApiResponse.class);
        }
        AuditResultVO auditResult = new AuditResultVO();
        auditResult.setApplyId(auditTask.getApplyId());
        auditResult.setFlowCode(auditTask.getFlowCode());
        auditResult.setTenantCode(auditTask.getTenantCode());
        auditResult.setProductCode(auditTask.getProductCode());
        auditResult.setUserId(auditTask.getUserId());
        auditResult.setUserName(auditTask.getUserName());
        auditResult.setMobile(auditTask.getMobile());
        auditResult.setIdCard(auditTask.getIdCard());
        auditResult.setOrderId(auditTask.getOrderId());
        auditResult.setIp(auditTask.getIp());
        auditResult.setDeviceFingerPrint(auditTask.getDeviceFingerPrint());
        auditResult.setTransactionTime(auditTask.getTransactionTime());
        auditResult.setCallbackUrl(auditTask.getCallbackUrl());
        auditResult.setTaskStatus(auditTask.getTaskStatus());
        return ApiResponse.success(auditResult);
    }

    @Override
    public void reapplyJury() {
        List<AuditTaskDO> auditTaskList = new ArrayList<>();
        List<AuditTaskDO> requestReceivedList = this.auditTaskMapper
                .listAuditTaskByTaskStatusAndRetryCount(AuditTaskStatusEnum.REQUEST_RECEIVED.getCode(), MAX_RETRY_COUNT);
        List<AuditTaskDO> forwardedFailList = this.auditTaskMapper
                .listAuditTaskByTaskStatusAndRetryCount(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), MAX_RETRY_COUNT);
        auditTaskList.addAll(requestReceivedList);
        auditTaskList.addAll(forwardedFailList);
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            Integer auditId = auditTask.getId();
            Integer retryCount = auditTask.getRetryCount() + 1;
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            String inputRawParam = auditTaskParam.getInputRawParam();
            ApplyRequest applyRequest = JSON.parseObject(inputRawParam, ApplyRequest.class);
            ApiResponse<String> applyResponse = this.juryApi.apply(applyRequest);
            if (applyResponse.isSuccess()) {
                auditTask.setTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode());
            } else {
                auditTask.setTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode());
            }
            auditTask.setRetryCount(retryCount);
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
        }
    }

    @Override
    public void varResultQuery() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            ApiResponse<VarResult> apiResponse = this.juryApi.getVarResult(auditTask.getApplyId(), auditTask.getTenantCode());
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
            String varResultString = auditTaskParam.getVarResult();
            VarResult varResult = JSON.parseObject(varResultString, VarResult.class);
            this.asyncProcessTask.startProcess(auditTask, varResult);
        }
    }

    @Override
    public void callbackTenant() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getId());
            String outputRawParam = auditTaskParam.getOutputRawParam();
            ApiResponse<JSONObject> apiResponse = JSON.parseObject(outputRawParam, ApiResponse.class);
            AuditResultVO auditResultVO = JSON.toJavaObject(apiResponse.getData(), AuditResultVO.class);
            this.asyncProcessTask.sendAuditResult(auditTask, ApiResponse.success(auditResultVO));
        }
    }
}