package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import com.ps.judge.provider.listener.AgendaEventListenerImpl;
import com.ps.judge.provider.listener.ProcessEventListenerImpl;
import com.ps.judge.provider.listener.RuleRuntimeEventListenerImpl;
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
            this.updateAuditStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), auditId);
            return;
        }
        this.updateAuditStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode(), auditId);
    }

    @Override
    @Async
    public void startProcess(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.CALLBACK_FAIL.getCode()) {
            return;
        }
        Integer taskId = auditTask.getId();
        String flowCode = auditTask.getFlowCode();
        ConfigFlowDO configFlow = this.configFlowMapper.getByFlowCode(flowCode);
        if (Objects.isNull(configFlow)) {
            this.updateAuditStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.getStatus()) {
            this.updateAuditStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }
        KieSession kieSession = this.kSessionManager.getKieSession(flowCode);
        if (Objects.isNull(kieSession)) {
            this.updateAuditStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode(), taskId);
            return;
        }

        kieSession.addEventListener(new AgendaEventListenerImpl());
        kieSession.addEventListener(new ProcessEventListenerImpl());
        kieSession.addEventListener(new RuleRuntimeEventListenerImpl());
        Map<String, Object> parameters = new HashMap<>();
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = new ArrayList<>();
        ScoreCardVO scoreCard = new ScoreCardVO();
        parameters.put("platform", varResult.getPlatform());
        parameters.put("merchant", varResult.getMerchant());
        parameters.put("product", varResult.getProduct());
        parameters.put("triggeredRuleList", auditTaskTriggeredRuleDOList);
        parameters.put("scoreCard", scoreCard);
        kieSession.startProcess(flowCode, parameters);
        kieSession.destroy();

        auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.getCode());
        auditTask.setCompleteTime(LocalDateTime.now());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        this.processResult(auditTask, parameters);
    }

    private void processResult(AuditTaskDO auditTask, Map<String, Object> parameters) {
        List<NodeResultVO> nodeResult = new ArrayList<>();
        ScoreCardVO scoreCard = (ScoreCardVO) parameters.get("scoreCard");
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = (List<AuditTaskTriggeredRuleDO>) parameters.get("triggeredRuleList");

        NodeResultVO node1 = new NodeResultVO();
        if (auditTaskTriggeredRuleDOList.isEmpty()) {
            node1.setIndex(1);
            if (StringUtils.equals(auditTask.getFlowCode(), "judge_old")) {
                node1.setRulePackageCode("ZRX02");
            } else {
                node1.setRulePackageCode("ZRX01");
            }
            node1.setAuditScore(0);
            node1.setAuditCode(AuditCodeEnum.PASS.toString());
            node1.setTriggeredRules(new ArrayList<>());
            nodeResult.add(node1);

            NodeResultVO node2 = new NodeResultVO();
            node2.setIndex(2);
            node2.setRulePackageCode("SC01");
            node2.setAuditScore(scoreCard.getScore());
            node2.setTriggeredRules(new ArrayList<>());
            nodeResult.add(node2);

            NodeResultVO node3 = new NodeResultVO();
            node3.setIndex(3);
            node3.setRulePackageCode("LOC01");
            node3.setAuditScore(Integer.valueOf(scoreCard.getQuota()));
            node3.setTriggeredRules(new ArrayList<>());
            nodeResult.add(node3);

            auditTask.setAuditScore(scoreCard.getScore());
        } else {
            int resultCode = 1;
            List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
            for (AuditTaskTriggeredRuleDO auditTaskTriggeredRuleDO : auditTaskTriggeredRuleDOList) {
                auditTaskTriggeredRuleDO.setTaskId(auditTask.getId());
                auditTaskTriggeredRuleDO.setTenantCode(auditTask.getTenantCode());
                auditTaskTriggeredRuleDO.setApplyId(auditTask.getApplyId());
                auditTaskTriggeredRuleDO.setFlowCode(auditTask.getFlowCode());
                auditTaskTriggeredRuleDO.setIndex(1);
                auditTaskTriggeredRuleDO.setGmtCreate(LocalDateTime.now());
                this.auditTaskTriggeredRuleMapper.insert(auditTaskTriggeredRuleDO);

                TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
                triggeredRuleVO.setRuleCode(auditTaskTriggeredRuleDO.getRuleCode());
                triggeredRuleVO.setRuleName(auditTaskTriggeredRuleDO.getRuleName());
                triggeredRuleVOList.add(triggeredRuleVO);
                if (StringUtils.equals(auditTaskTriggeredRuleDO.getRulePackageCode(), "ZRX01")) {
                    resultCode = resultCode & Integer.parseInt(auditTaskTriggeredRuleDO.getResult());
                } else {
                    resultCode = Integer.parseInt(auditTaskTriggeredRuleDO.getResult());
                }
            }

            node1.setIndex(1);
            node1.setRulePackageCode(auditTaskTriggeredRuleDOList.get(0).getRulePackageCode());
            node1.setAuditCode(AuditCodeEnum.getAuditCode(resultCode));
            node1.setAuditScore(0);
            node1.setTriggeredRules(triggeredRuleVOList);
            nodeResult.add(node1);
        }

        auditTask.setAuditCode(node1.getAuditCode());
        ApiResponse<AuditResultVO> apiResponse = this.saveOutputRawParam(auditTask, nodeResult);
        auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        auditTask.setGmtModified(LocalDateTime.now());
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
        auditResult.setAuditScore(auditTask.getAuditScore());
        auditResult.setNodeResult(nodeResult);
        ApiResponse<AuditResultVO> apiResponse = ApiResponse.success(auditResult);
        String apiResponseString = JSON.toJSONString(apiResponse, SerializerFeature.WriteMapNullValue);
        this.auditTaskParamMapper.updateOutputRawParam(apiResponseString, auditTask.getId(), LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse) {
        Integer auditId = auditTask.getId();
        int callbackCount = auditTask.getCallbackCount();
        if (callbackCount >= 3) {
            this.updateAuditStatus(AuditTaskStatusEnum.CALLBACK_FAIL.getCode(), auditId);
            return;
        }
        auditTask.setCallbackCount(++callbackCount);
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        if (this.sendPost(auditTask.getCallbackUrl(), apiResponse)) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode());
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
        }
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

    private boolean updateAuditStatus(int status, int taskId) {
        return this.auditTaskMapper.updateTaskStatus(status, taskId, LocalDateTime.now()) > 0;
    }

}
