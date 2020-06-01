package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AuditTaskTriggeredRuleMapper auditTaskTriggeredRuleMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpHeaders httpHeaders;
    @Autowired
    AsyncProcessTask asyncProcessTask;

    @Override
    public AuditTaskDO getAuditTask(String tenantCode, String applyId) {
        return this.auditTaskMapper.getAuditTask(tenantCode, applyId);
    }

    @Override
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

        Integer auditId = auditTask.getId();
        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getTenantCode());
        auditTaskParam.setTaskId(auditId);
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(JSON.toJSONString(request));
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setVarResult(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);

        ApiResponse<String> applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), auditId);
            return ApiResponse.error(applyResponse.getCode(), applyResponse.getMessage());
        }
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditId);
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public ApiResponse saveVarResult(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode()) {
            return ApiResponse.success();
        }
        Integer auditId = auditTask.getId();
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED.getCode(), auditId);
        this.auditTaskParamMapper.updateVarResult(JSON.toJSONString(varResult), auditId);
        auditTask.setTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED.getCode());
        this.asyncProcessTask.startProcess(auditTask, varResult);
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
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            Integer auditId = auditTask.getId();
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            String inputRawParam = auditTaskParam.getInputRawParam();
            ApplyRequest applyRequest = JSON.parseObject(inputRawParam, ApplyRequest.class);
            ApiResponse<String> applyResponse = this.juryApi.apply(applyRequest);
            if (applyResponse.isSuccess()) {
                this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditId);
            }
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
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED.getCode());
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
            this.sendAuditResult(auditTask, ApiResponse.success(auditResultVO));
        }
    }

    @Override
    public void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse) {
        int callbackCount = auditTask.getCallbackCount();
        if (callbackCount > 3) {
            return;
        }
        callbackCount++;
        Integer auditId = auditTask.getId();
        this.auditTaskMapper.updateCallbackCount(callbackCount, auditId);
        if (this.sendPost(auditTask.getCallbackUrl(), apiResponse)) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode(), auditId);
            return;
        }
        if (callbackCount == 3) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK_FAIL.getCode(), auditId);
        }
    }

    private boolean sendPost(String url, ApiResponse<AuditResultVO> apiResponse) {
        HttpEntity<ApiResponse<AuditResultVO>> requestEntity = new HttpEntity(apiResponse, httpHeaders);
        ResponseEntity<JSONObject> result = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
        if (result.getStatusCode() == HttpStatus.OK) {
            JSONObject body = result.getBody();
            if (Objects.isNull(body)) {
                return false;
            }
            Integer resultCode = body.getInteger("code");
            if (Objects.isNull(resultCode)) {
                return false;
            }
            if (resultCode == ApiResponse.success().getCode()) {
                return true;
            }
        }
        return false;
    }

}