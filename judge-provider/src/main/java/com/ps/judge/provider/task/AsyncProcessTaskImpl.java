package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.NodeResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.provider.enums.AuditCodeEnum;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.CallbackService;
import com.ps.judge.provider.service.FlowService;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.common.JuryApply;
import com.ps.jury.api.request.ApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
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
    CallbackService callbackService;


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
    public void startProcess(AuditTaskDO auditTask, Map map) {
        if (!syncAuditTaskStatus(auditTask)) {
            return;
        }

        Map<String, Object> varResultMap = this.getVarResultMap((Map) map.get("varResult"));
        if (Objects.isNull(varResultMap)) {
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            return;
        }
        List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList = new ArrayList<>();

        List<Object> paramList = new ArrayList<>();
        paramList.add(varResultMap);
        paramList.add(auditTaskTriggeredRuleDOList);
        try {
            if (!this.flowService.executorFlow(auditTask.getFlowCode(), paramList)) {
                this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
                return;
            }
            this.processResult(auditTask, auditTaskTriggeredRuleDOList);
        } catch (Exception e) {
            log.error("规则流flowCode : {} , 执行异常，异常原因 ：{}", auditTask.getFlowCode(), e.getMessage());
            this.updateAuditStatus(auditTask, AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value());
            throw new RuntimeException(e);
        }
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
