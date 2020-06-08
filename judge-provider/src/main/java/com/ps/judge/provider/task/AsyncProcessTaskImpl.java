package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.enums.AuditCodeEnum;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class AsyncProcessTaskImpl implements AsyncProcessTask {
    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AuditTaskTriggeredRuleMapper auditTaskTriggeredRuleMapper;
    @Autowired
    ConfigFlowMapper configFlowMapper;
    @Autowired
    KSessionManager kSessionManager;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpHeaders httpHeaders;

    @Override
    @Async
    public void applyJury(int auditId, ApplyRequest request) {
        ApiResponse<String> applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), auditId);
            return;
        }
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditId);
    }

    @Override
    @Async
    public void startProcess(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.VAR_ACCEPTED_SUCCESS.getCode()) {
            return;
        }
        Integer taskId = auditTask.getId();
        String flowCode = auditTask.getFlowCode();
        ConfigFlowDO configFlow = this.configFlowMapper.getByFlowCode(flowCode);
        if (Objects.isNull(configFlow)) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.status()) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }
        KieSession kieSession = this.kSessionManager.getKieSession(flowCode);
        if (Objects.isNull(kieSession)) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        List<AuditTaskTriggeredRuleDO> triggeredRuleList = new ArrayList<>();
        map.put("platform", varResult.getPlatform());
        map.put("merchant", varResult.getMerchant());
        map.put("product", varResult.getProduct());
        map.put("triggeredRuleList", triggeredRuleList);
        kieSession.startProcess(flowCode, map);
        kieSession.dispose();

        triggeredRuleList = (List<AuditTaskTriggeredRuleDO>) map.get("triggeredRuleList");
        if (triggeredRuleList.isEmpty()) {
            auditTask.setAuditCode(AuditCodeEnum.PASS.toString());
        } else {
            auditTask.setAuditCode(AuditCodeEnum.REJECT.toString());
        }
        auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.getCode());
        auditTask.setCompleteTime(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);

        List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
        for (AuditTaskTriggeredRuleDO triggeredRule : triggeredRuleList) {
            triggeredRule.setTaskId(taskId);
            triggeredRule.setTenantCode(auditTask.getTenantCode());
            triggeredRule.setApplyId(auditTask.getApplyId());
            triggeredRule.setFlowCode(flowCode);
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
        auditResult.setTaskStatus(auditTask.getTaskStatus());
        auditResult.setAuditCode(auditResult.getAuditCode());
        auditResult.setTriggeredRules(triggeredRuleVOList);

        ApiResponse<AuditResultVO> apiResponse = ApiResponse.success(auditResult);
        this.auditTaskParamMapper.updateOutputRawParam(JSON.toJSONString(apiResponse), taskId);
        auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        this.auditTaskMapper.updateTaskStatus(auditTask.getTaskStatus(), taskId);
        this.sendAuditResult(auditTask, apiResponse);
    }

    @Override
    public void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse) {
        Integer auditId = auditTask.getId();
        int callbackCount = auditTask.getCallbackCount();
        if (callbackCount >= 3) {
            this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK_FAIL.getCode(), auditId);
            return;
        }
        if (this.sendPost(auditTask.getCallbackUrl(), apiResponse)) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode());
        }
        auditTask.setCallbackCount(++callbackCount);
        this.auditTaskMapper.update(auditTask);
    }

    private boolean sendPost(String url, ApiResponse<AuditResultVO> apiResponse) {
        HttpEntity<ApiResponse<AuditResultVO>> requestEntity = new HttpEntity<>(apiResponse, httpHeaders);
        ResponseEntity<JSONObject> result = this.restTemplate.exchange(url , HttpMethod.POST, requestEntity, JSONObject.class);
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
