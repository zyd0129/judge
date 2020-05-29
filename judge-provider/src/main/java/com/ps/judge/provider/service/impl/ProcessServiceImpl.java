package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.apache.commons.lang.StringUtils;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

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
    KSessionManager kSessionManager;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpHeaders httpHeaders;

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

        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getTenantCode());
        auditTaskParam.setTaskId(auditTask.getId());
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(JSON.toJSONString(request));
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);

        ApiResponse<String> applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), auditTask.getTenantCode(), auditTask.getApplyId());
            return ApiResponse.error(applyResponse.getCode(), applyResponse.getMessage());
        }
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditTask.getTenantCode(), auditTask.getApplyId());
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public void startProcess(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode()) {
            return;
        }
        String tenantCode = auditTask.getTenantCode();
        String applyId = auditTask.getApplyId();
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.VAR_ACCEPTED.getCode(), tenantCode, applyId);
        System.out.println("updateTaskStatus VAR_ACCEPTED");

        System.out.println("flowCode " + auditTask.getFlowCode());
        KieSession kieSession = this.kSessionManager.getKieSession(auditTask.getFlowCode());
        System.out.println("kieSession " + kieSession);
        /*String url = "http://192.168.159.129:8080/business-central/maven2" +
                "/com/ps/jury/api/objects/judge/1.0.0/judge-1.0.0.jar";
        */

        Map<String, Object> map = new HashMap<>();
        List<AuditTaskTriggeredRuleDO> triggeredRuleList = new ArrayList<>();
        map.put("system", varResult.getSystem());
        map.put("merchant", varResult.getMerchant());
        map.put("product", varResult.getProduct());
        map.put("triggeredRuleList", triggeredRuleList);

        kieSession.startProcess(auditTask.getFlowCode(), map);
        kieSession.dispose();
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE.getCode(), tenantCode, applyId);
        System.out.println("updateTaskStatus AUDIT_COMPLETE");
        //todo 更新 task status 和 AuditCode AuditScore

        triggeredRuleList = (List<AuditTaskTriggeredRuleDO>) map.get("triggeredRuleList");
        System.out.println("triggeredRuleList" + triggeredRuleList.size());
          System.out.println("triggeredRuleList" + triggeredRuleList);
        List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
        for (AuditTaskTriggeredRuleDO triggeredRule : triggeredRuleList) {
            triggeredRule.setTaskId(auditTask.getId());
            triggeredRule.setTenantCode(auditTask.getTenantCode());
            triggeredRule.setApplyId(auditTask.getApplyId());
            triggeredRule.setFlowCode(auditTask.getFlowCode());
            triggeredRule.setRuleName("");
            triggeredRule.setRuleVersion("");
            triggeredRule.setRulePackageCode("");
            triggeredRule.setRulePackageName("");
            triggeredRule.setRulePackageVersion("");
            triggeredRule.setParam("");
            triggeredRule.setExpression("");
            triggeredRule.setGmtCreate(LocalDateTime.now());
            this.auditTaskTriggeredRuleMapper.insert(triggeredRule);
            TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
            triggeredRuleVO.setRulePackageCode(triggeredRule.getRulePackageCode());
            triggeredRuleVO.setRuleCode(triggeredRule.getRuleCode());
            triggeredRuleVO.setRuleName(triggeredRule.getRuleName());
            triggeredRuleVO.setExpression(triggeredRule.getExpression());
            triggeredRuleVO.setParam(triggeredRule.getParam());
            triggeredRuleVOList.add(triggeredRuleVO);
        }

        AuditResultVO auditResult = new AuditResultVO();
        auditResult.setApplyId(varResult.getApplyId());
        auditResult.setFlowCode(varResult.getFlowCode());
        auditResult.setTenantCode(varResult.getTenantCode());
        auditResult.setProductCode(varResult.getProductCode());
        auditResult.setUserId(varResult.getUserId());
        auditResult.setUserName(varResult.getUserName());
        auditResult.setMobile(varResult.getMobile());
        auditResult.setIdCard(varResult.getIdCard());
        auditResult.setOrderId(varResult.getOrderId());
        auditResult.setIp(varResult.getIp());
        auditResult.setDeviceFingerPrint(varResult.getDeviceFingerPrint());
        auditResult.setTransactionTime(varResult.getTransactionTime());
        auditResult.setCallbackUrl(varResult.getCallbackUrl());
        auditResult.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE.getCode());
        //auditResult.setAuditCode();
        //auditResult.setAuditScore();
        auditResult.setTriggeredRules(triggeredRuleVOList);

        ApiResponse<AuditResultVO> apiResponse = ApiResponse.success(auditResult);
        this.auditTaskParamMapper.updateOutputRawParam(JSON.toJSONString(apiResponse), tenantCode, applyId);
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode(), tenantCode, applyId);
        System.out.println("updateTaskStatus CALLBACK");
        this.sendAuditResult(auditTask, apiResponse);
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO auditTask) {
        AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
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
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
            String inputRawParam = auditTaskParam.getInputRawParam();
            ApplyRequest applyRequest = JSON.parseObject(inputRawParam, ApplyRequest.class);
            ApiResponse<String> applyResponse = this.juryApi.apply(applyRequest);
            if (applyResponse.isSuccess()) {
                this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditTask.getTenantCode(), auditTask.getApplyId());
                return;
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
            if(!apiResponse.isSuccess()) {
                return;
            }
            this.startProcess(auditTask, apiResponse.getData());
        }
    }

    @Override
    public void callbackTenant() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper
                    .getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
            String outputRawParam = auditTaskParam.getOutputRawParam();
            ApiResponse<JSONObject> apiResponse = JSON.parseObject(outputRawParam, ApiResponse.class);
            AuditResultVO auditResultVO = JSON.toJavaObject(apiResponse.getData(), AuditResultVO.class);
            this.sendAuditResult(auditTask, ApiResponse.success(auditResultVO));
        }
    }

    private void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse) {
        int callbackCount = auditTask.getCallbackCount();
        if (callbackCount > 3) {
            return;
        }
        callbackCount++;
        String tenantCode = auditTask.getTenantCode();
        String applyId = auditTask.getApplyId();
        this.auditTaskMapper.updateCallbackCount(callbackCount, tenantCode, applyId);
        if (this.sendPost(auditTask.getCallbackUrl(), apiResponse)) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode(), tenantCode, applyId);
            return;
        }
        if(callbackCount == 3) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK_FAIL.getCode(), tenantCode, applyId);
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