package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.NodeResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.entity.ScoreCardVO;
import com.ps.judge.provider.enums.AuditCodeEnum;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.apache.commons.lang.StringUtils;
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
        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK_FAIL.getCode()) {
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

        Map<String, Object> parameters = new HashMap<>();
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = new ArrayList<>();
        ScoreCardVO scoreCard = new ScoreCardVO();
        parameters.put("platform", varResult.getPlatform());
        parameters.put("merchant", varResult.getMerchant());
        parameters.put("product", varResult.getProduct());
        parameters.put("triggeredRuleList", auditTaskTriggeredRuleDOList);
        parameters.put("scoreCard", scoreCard);
        kieSession.startProcess(flowCode, parameters);
        kieSession.dispose();

        System.err.println(parameters.get("scoreCard"));
        scoreCard = (ScoreCardVO) parameters.get("scoreCard");
        auditTaskTriggeredRuleDOList = (List<AuditTaskTriggeredRuleDO>) parameters.get("triggeredRuleList");
        auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.getCode());
        auditTask.setCompleteTime(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);

        Map<String, List<AuditTaskTriggeredRuleDO>> triggeredRuleVOMap = new HashMap<>();
        for (AuditTaskTriggeredRuleDO auditTaskTriggeredRuleDO : auditTaskTriggeredRuleDOList) {
            auditTaskTriggeredRuleDO.setTaskId(taskId);
            auditTaskTriggeredRuleDO.setTenantCode(auditTask.getTenantCode());
            auditTaskTriggeredRuleDO.setApplyId(auditTask.getApplyId());
            auditTaskTriggeredRuleDO.setFlowCode(flowCode);
            auditTaskTriggeredRuleDO.setIndex(1);
            auditTaskTriggeredRuleDO.setGmtCreate(LocalDateTime.now());
            this.auditTaskTriggeredRuleMapper.insert(auditTaskTriggeredRuleDO);

            List<AuditTaskTriggeredRuleDO> triggeredRuleDOList = triggeredRuleVOMap.get(auditTaskTriggeredRuleDO.getRulePackageCode());
            if (Objects.isNull(triggeredRuleDOList)) {
                triggeredRuleDOList = new ArrayList<>();
                triggeredRuleDOList.add(auditTaskTriggeredRuleDO);
                triggeredRuleVOMap.put(auditTaskTriggeredRuleDO.getRulePackageCode(), triggeredRuleDOList);
            } else {
                triggeredRuleDOList.add(auditTaskTriggeredRuleDO);
            }
        }

        List<NodeResultVO> nodeResult = new ArrayList<>();
        for (Map.Entry<String, List<AuditTaskTriggeredRuleDO>> entry : triggeredRuleVOMap.entrySet()) {
            List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleList = entry.getValue();
            NodeResultVO nodeResultVO = new NodeResultVO();
            nodeResultVO.setRulePackageCode(entry.getKey());
            int resultCode = 1;
            List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
            for (AuditTaskTriggeredRuleDO auditTaskTriggeredRuleDO : auditTaskTriggeredRuleList) {
                TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
                triggeredRuleVO.setRuleCode(auditTaskTriggeredRuleDO.getRuleCode());
                triggeredRuleVO.setRuleName(auditTaskTriggeredRuleDO.getRuleName());
                triggeredRuleVO.setExpression(auditTaskTriggeredRuleDO.getExpression());
                triggeredRuleVO.setParam(auditTaskTriggeredRuleDO.getParam());
                triggeredRuleVOList.add(triggeredRuleVO);

                if (StringUtils.equals(entry.getKey(), "ZRX01")) {
                    resultCode = resultCode & Integer.parseInt(auditTaskTriggeredRuleDO.getResult());
                } else {
                    resultCode = Integer.parseInt(auditTaskTriggeredRuleDO.getResult());
                }
            }
            nodeResultVO.setAuditCode(AuditCodeEnum.getAuditCode(resultCode));
            nodeResultVO.setAuditScore(scoreCard.getScore());
            nodeResultVO.setTriggeredRules(triggeredRuleVOList);
            nodeResult.add(nodeResultVO);
        }

        if (nodeResult.isEmpty()) {
            auditTask.setAuditCode(AuditCodeEnum.PASS.toString());
        } else {
            auditTask.setAuditCode(nodeResult.get(0).getAuditCode());
        }

        ApiResponse<AuditResultVO> apiResponse = this.saveOutputRawParam(auditTask, nodeResult);
        auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        this.auditTaskMapper.update(auditTask);
        this.sendAuditResult(auditTask, apiResponse);
    }

    private ApiResponse<AuditResultVO> saveOutputRawParam(AuditTaskDO auditTask, List<NodeResultVO> nodeResult) {
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
        auditResult.setAuditCode(auditTask.getAuditCode());
        auditResult.setAuditScore(0);
        auditResult.setNodeResult(nodeResult);
        ApiResponse<AuditResultVO> apiResponse = ApiResponse.success(auditResult);
        this.auditTaskParamMapper.updateOutputRawParam(JSON.toJSONString(apiResponse), auditTask.getId());
        return apiResponse;
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
