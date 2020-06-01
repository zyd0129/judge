package com.ps.judge.provider.task;

import com.alibaba.fastjson.JSON;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.response.VarResult;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AsyncProcessTask {
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AuditTaskTriggeredRuleMapper auditTaskTriggeredRuleMapper;
    @Autowired
    KSessionManager kSessionManager;
    @Autowired
    ProcessService processService;

    @Async
    public void startProcess(AuditTaskDO auditTask, VarResult varResult) {
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.VAR_ACCEPTED.getCode()) {
            return;
        }
        Integer auditId = auditTask.getId();
        KieSession kieSession = this.kSessionManager.getKieSession(auditTask.getFlowCode());

        Map<String, Object> map = new HashMap<>();
        List<AuditTaskTriggeredRuleDO> triggeredRuleList = new ArrayList<>();
        map.put("system", varResult.getSystem());
        map.put("merchant", varResult.getMerchant());
        map.put("product", varResult.getProduct());
        map.put("triggeredRuleList", triggeredRuleList);

        kieSession.startProcess(auditTask.getFlowCode(), map);
        kieSession.dispose();
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.AUDIT_COMPLETE.getCode(), auditId);
        //todo 更新 AuditCode AuditScore

        triggeredRuleList = (List<AuditTaskTriggeredRuleDO>) map.get("triggeredRuleList");

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
        this.auditTaskParamMapper.updateOutputRawParam(JSON.toJSONString(apiResponse), auditId);
        this.auditTaskMapper.updateTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode(), auditId);
        this.processService.sendAuditResult(auditTask, apiResponse);
    }


}
