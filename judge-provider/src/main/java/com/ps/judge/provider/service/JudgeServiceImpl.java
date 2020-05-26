package com.ps.judge.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
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
import org.kie.api.runtime.KieSessionsPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;

    @Autowired
    AuditTaskTriggeredRuleMapper auditTaskTriggeredRuleMapper;

    @Override
    public AuditTaskDO getAuditByTenantIdAndApplyId(String tenantId, String applyId) {
        return this.auditTaskMapper.getAuditTaskByTenantIdAndApplyId(tenantId, applyId);
    }

    @Override
    public ApiResponse<ApplyResultVO> apply(ApplyRequest request) {
        AuditTaskDO risk = new AuditTaskDO();
        risk.setApplyId(request.getApplyId());
        risk.setFlowCode(request.getFlow());
        risk.setTenantCode(request.getTenantId());
        risk.setProductCode(request.getProductId());
        risk.setUserId(request.getUserId());
        risk.setUserName(request.getUserName());
        risk.setMobile(request.getMobile());
        risk.setIdCard(request.getIdCard());
        risk.setOrderId(request.getOrderId());
        risk.setIp(request.getIp());
        risk.setDeviceFingerPrint(request.getDeviceFingerPrint());
        risk.setTransactionTime(request.getTransactionTime());
        //risk.setTaskStatus();
        risk.setCallbackUrl(request.getCallbackUrl());
        risk.setGmtCreate(LocalDateTime.now());
        risk.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.insert(risk);

        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getApplyId());
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(JSONObject.toJSONString(request));
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);
        // TODO: 2020/5/19 缓存进件
        
        ApiResponse applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            return ApiResponse.error(applyResponse.getCode(), applyResponse.getMessage());
        }
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public void startProcess(VarResult varResult) {

        String url = "http://192.168.159.129:8080/business-central/maven2" +
                "/com/ps/jury/api/objects/judge/1.0.0/judge-1.0.0.jar";
        //Kie服务
        KieServices kieServices = KieServices.Factory.get();
        //Kie资料库
        KieRepository kieRepository = kieServices.getRepository();

        UrlResource urlResource = (UrlResource) kieServices.getResources().newUrlResource(url);
        urlResource.setBasicAuthentication("enabled");//开启认证
        urlResource.setPassword("admin");
        urlResource.setUsername("admin");
        InputStream is = null;
        try {
            is = urlResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取资源
        Resource resource = kieServices.getResources().newInputStreamResource(is);
        //获取加载资源获取KieModule
        KieModule kieModule = kieRepository.addKieModule(resource);

        //通过kieModule的ReleaseId获取kieContainer
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        KieSession kieSession = kieContainer.newKieSession();

        KieSessionsPool kieSessionsPool = kieContainer.newKieSessionsPool(20);

        //kieSession.addEventListener(new ProcessEventListenerImpl());
        //kieSession.addEventListener(new RuleRuntimeEventListenerImpl());

        //FactHandle factHandle = kieSession.insert(person);
        //System.out.println(factHandle);

        Map<String, Object> map = new HashMap();
        map.put("system", varResult.getSystem());
        map.put("merchant", varResult.getMerchant());
        map.put("product", varResult.getProduct());
        kieSession.startProcess("judge", map);
        kieSession.dispose();

        // TODO: 2020/5/19 推送风控结果
        // TODO: 2020/5/19 推送成功，删除出件
    }

    @Override
    public AuditResultVO getAuditResult(AuditTaskDO auditTask) {
        AuditResultVO auditResult = new AuditResultVO();
        auditResult.setApplyId(auditTask.getApplyId());
        auditResult.setFlow(auditTask.getFlowCode());
        auditResult.setTenantId(auditTask.getTenantCode());
        auditResult.setProductId(auditTask.getProductCode());
        auditResult.setUserId(auditTask.getUserId());
        auditResult.setUserName(auditTask.getUserName());
        auditResult.setMobile(auditTask.getMobile());
        auditResult.setIdCard(auditTask.getIdCard());
        auditResult.setOrderId(auditTask.getOrderId());
        auditResult.setIp(auditTask.getIp());
        auditResult.setDeviceFingerPrint(auditTask.getDeviceFingerPrint());
        auditResult.setTransactionTime(auditTask.getTransactionTime());
        auditResult.setCallbackUrl(auditTask.getCallbackUrl());
        auditResult.setAuditCode(auditTask.getAuditCode());
        auditResult.setAuditScore(auditTask.getAuditScore());
        auditResult.setAuditStatus(auditTask.getTaskStatus());

        List<TriggeredRuleVO> triggeredRules = new ArrayList<>();
        List<AuditTaskTriggeredRuleDO> triggeredRuleList = this.auditTaskTriggeredRuleMapper
                .listTriggeredRuleLogByTenantIdAndApplyId(auditTask.getTenantCode(), auditTask.getApplyId());
        for(AuditTaskTriggeredRuleDO auditTaskTriggeredRuleDO : triggeredRuleList){
            //RuleDO rule = this.ruleMapper.getRuleByCode(auditTaskTriggeredRuleDO.getRuleCode());
            TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
           /* triggeredRuleVO.setRulePackageCode(rule.getCode());
            triggeredRuleVO.setRuleCode(auditTaskTriggeredRuleDO.getRuleCode());
            triggeredRuleVO.setRuleName(rule.getName());
            triggeredRuleVO.setExpression(rule.getExpression());
            triggeredRuleVO.setParam(auditTaskTriggeredRuleDO.getParam());
            triggeredRuleVO.setTriggeredResult(rule.getResult());*/
            triggeredRules.add(triggeredRuleVO);
        }

        auditResult.setTriggeredRules(triggeredRules);
        return auditResult;
    }

}
