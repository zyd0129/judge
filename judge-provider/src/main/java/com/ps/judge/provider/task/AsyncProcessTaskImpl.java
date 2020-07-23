package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
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
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.entity.ScoreCardVO;
import com.ps.judge.provider.enums.AuditCodeEnum;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.judge.provider.rule.executor.RuleExecutor;
import com.ps.judge.provider.service.CallbackService;
import com.ps.judge.provider.service.FlowService;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.common.JuryApply;
import com.ps.jury.api.request.ApplyRequest;
import org.apache.commons.lang.StringUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    FlowService flowService;
    @Autowired
    KSessionManager kSessionManager;
    @Autowired
    CallbackService callbackService;
    @Autowired
    RuleExecutor ruleExecutor;

    @Override
    @Async
    public void applyJury(AuditTaskDO auditTask, ApplyRequest request) {
        ApiResponse<JuryApply> applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.FORWARDED_FAIL.value());
            return;
        }
        this.updateAuditStatus(auditTask, AuditTaskStatusEnum.FORWARDED_SUCCESS.value());
    }

    @Override
    @Async
    @Transactional
    public void startProcess(AuditTaskDO auditTask, Map map) {
        if (!syncAuditTaskStatus(auditTask)) {
            return;
        }
        Map<String, Object> varResultMap = this.getVarResultMap((Map) map.get("varResult"));
        if (Objects.isNull(varResultMap)) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            return;
        }
        String flowCode = auditTask.getFlowCode();
        ConfigFlowDO configFlow = this.flowService.getByFlowCode(flowCode);
        if (Objects.isNull(configFlow)) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            return;
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.value()) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            return;
        }
        KieSession kieSession = null;
        if (configFlow.getLoadMethod() == 0) {
            kieSession = this.flowService.getKieSession(configFlow.getFlowCode());
        } else {
            kieSession = this.kSessionManager.getKieSession(flowCode);
        }

        if (Objects.isNull(kieSession)) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            return;
        }

        ScoreCardVO scoreCard = new ScoreCardVO();
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = new ArrayList<>();

        if (configFlow.getLoadMethod() == 0) {
            List<Object> paramList = new ArrayList<>();
            paramList.add(varResultMap);
            paramList.add(auditTaskTriggeredRuleDOList);
            try {
                this.ruleExecutor.executor(kieSession, paramList, "");
            } catch (Exception e) {
                auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
                auditTask.setCompleteTime(LocalDateTime.now());
                auditTask.setGmtModified(LocalDateTime.now());
                this.auditTaskMapper.update(auditTask);
            } finally {
                kieSession.destroy();
            }
            this.processResult(auditTask, auditTaskTriggeredRuleDOList);
            return;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("map", varResultMap);
        parameters.put("triggeredRuleList", auditTaskTriggeredRuleDOList);
        parameters.put("scoreCard", scoreCard);

        try {
            kieSession.startProcess(flowCode, parameters);
        } catch (Exception e) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            auditTask.setCompleteTime(LocalDateTime.now());
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
        } finally {
            kieSession.destroy();
        }
        auditTask.setTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE_SUCCESS.value());
        auditTask.setCompleteTime(LocalDateTime.now());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        this.processResult(auditTask, parameters);
    }

    private Map<String, Object> getVarResultMap(Map<String, Object> levelMap) {
        if (Objects.isNull(levelMap)) {
            return null;
        }

        Map<String, Object> varResultMap = new HashMap<>();
        for (Map.Entry<String, Object> level : levelMap.entrySet()) {
            if (Objects.isNull(level.getValue())) {
                continue;
            }
            Map<String, Object> groupMap = (Map<String, Object>) level.getValue();
            for (Map.Entry<String, Object> group : groupMap.entrySet()) {
                if (Objects.isNull(group.getValue())) {
                    continue;
                }
                Map<String, Object> varMap = (Map<String, Object>) group.getValue();
                for (Map.Entry<String, Object> var : varMap.entrySet()) {
                    if (Objects.isNull(var.getValue())) {
                        continue;
                    }
                    varResultMap.put(level.getKey() + "_" + group.getKey() + "_" + var.getKey(), var.getValue());
                }
            }
        }
        return varResultMap;
    }

    @Transactional
    public boolean syncAuditTaskStatus(AuditTaskDO auditTask) {
        auditTask = this.auditTaskMapper.getAuditTaskByIdForUpdate(auditTask.getId());
        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.VAR_ACCEPTED_SUCCESS.value()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value()) {
            return this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT.value());
        }
        return false;
    }

    public void processResult(AuditTaskDO auditTask, List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList) {
        List<NodeResultVO> nodeResult = new ArrayList<>();
        NodeResultVO node1 = new NodeResultVO();
        if (auditTaskTriggeredRuleDOList.isEmpty()) {
            node1.setIndex(1);
            node1.setRulePackageCode("IDNAR");
            node1.setAuditScore(0);
            node1.setAuditCode(AuditCodeEnum.PASS.toString());
            node1.setTriggeredRules(new ArrayList<>());
            nodeResult.add(node1);
        } else {
            int resultCode = 1;
            List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
            for (AuditTaskTriggeredRuleDO auditTaskTriggeredRuleDO : auditTaskTriggeredRuleDOList) {
                BeanUtils.copyProperties(auditTask, auditTaskTriggeredRuleDO);
                auditTaskTriggeredRuleDO.setTaskId(auditTask.getId());
                auditTaskTriggeredRuleDO.setIndex(1);
                auditTaskTriggeredRuleDO.setGmtCreate(LocalDateTime.now());
                this.auditTaskTriggeredRuleMapper.insert(auditTaskTriggeredRuleDO);

                TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
                BeanUtils.copyProperties(auditTaskTriggeredRuleDO, triggeredRuleVO);
                triggeredRuleVOList.add(triggeredRuleVO);
                resultCode = resultCode & Integer.parseInt(auditTaskTriggeredRuleDO.getResult());
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
        auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK.value());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        this.callbackService.sendAuditTaskResult(auditTask, apiResponse);
    }

    public void processResult(AuditTaskDO auditTask, Map<String, Object> parameters) {
        List<NodeResultVO> nodeResult = new ArrayList<>();
        ScoreCardVO scoreCard = (ScoreCardVO) parameters.get("scoreCard");
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = (List<AuditTaskTriggeredRuleDO>) parameters.get("triggeredRuleList");

        NodeResultVO node1 = new NodeResultVO();
        if (auditTaskTriggeredRuleDOList.isEmpty()) {
            node1.setIndex(1);
            node1.setRulePackageCode("ZRX01");
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
                BeanUtils.copyProperties(auditTask, auditTaskTriggeredRuleDO);
                auditTaskTriggeredRuleDO.setTaskId(auditTask.getId());
                auditTaskTriggeredRuleDO.setIndex(1);
                auditTaskTriggeredRuleDO.setGmtCreate(LocalDateTime.now());
                this.auditTaskTriggeredRuleMapper.insert(auditTaskTriggeredRuleDO);

                TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
                BeanUtils.copyProperties(auditTaskTriggeredRuleDO, triggeredRuleVO);
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
        auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK.value());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        this.callbackService.sendAuditTaskResult(auditTask, apiResponse);
    }

    private ApiResponse<AuditResultVO> saveOutputRawParam(AuditTaskDO auditTask, List<NodeResultVO> nodeResult) {
        AuditResultVO auditResult = new AuditResultVO();
        BeanUtils.copyProperties(auditTask, auditResult);
        auditResult.setNodeResult(nodeResult);
        ApiResponse<AuditResultVO> apiResponse = ApiResponse.success(auditResult);
        String apiResponseString = JSON.toJSONString(apiResponse, SerializerFeature.WriteMapNullValue);
        this.auditTaskParamMapper.updateOutputRawParam(apiResponseString, auditTask.getId(), LocalDateTime.now());
        return apiResponse;
    }

    private boolean updateAuditStatus(AuditTaskDO auditTask, int status) {
        auditTask.setTaskStatus(status);
        auditTask.setGmtModified(LocalDateTime.now());
        return this.auditTaskMapper.update(auditTask) > 0;
    }
}
