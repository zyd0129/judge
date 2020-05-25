package com.ps.judge.provider.service;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.TriggeredRuleVO;
import com.ps.judge.dao.entity.RiskDO;
import com.ps.judge.dao.entity.RiskParamDO;
import com.ps.judge.dao.entity.RuleDO;
import com.ps.judge.dao.entity.TriggeredRuleDO;
import com.ps.judge.dao.mapper.RiskParamMapper;
import com.ps.judge.dao.mapper.RiskMapper;
import com.ps.judge.dao.mapper.RuleMapper;
import com.ps.judge.dao.mapper.TriggeredRuleMapper;
import com.ps.judge.provider.listener.AgendaEventListenerImpl;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.objects.common.ApiResponse;
import com.ps.jury.api.objects.request.ApplyRequest;
import com.ps.jury.api.objects.response.VarResult;
import org.apache.commons.lang.StringUtils;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.process.ProcessInstance;
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
    RiskMapper riskMapper;
    @Autowired
    RiskParamMapper riskParamMapper;
    @Autowired
    RuleMapper ruleMapper;
    @Autowired
    TriggeredRuleMapper triggeredRuleMapper;
    @Autowired
    AgendaEventListener agendaEventListener;

    @Override
    public RiskDO getAuditByTenantIdAndApplyId(String tenantId, String applyId) {
        return this.riskMapper.getRiskByTenantIdAndApplyId(tenantId, applyId);
    }

    @Override
    public ApiResponse<ApplyResultVO> apply(ApplyRequest request) {
        RiskDO risk = new RiskDO();
        risk.setApplyId(request.getApplyId());
        risk.setFlow(request.getFlow());
        risk.setTenantId(request.getTenantId());
        risk.setProductId(request.getProductId());
        risk.setUserId(request.getUserId());
        risk.setUserName(request.getUserName());
        risk.setMobile(request.getMobile());
        risk.setIdCard(request.getIdCard());
        risk.setOrderId(request.getOrderId());
        risk.setIp(request.getIp());
        risk.setDeviceFingerPrint(request.getDeviceFingerPrint());
        risk.setTransactionTime(request.getTransactionTime());
       // risk.setAuditStatus();
        risk.setCallbackUrl(request.getCallbackUrl());
        risk.setCreateTime(LocalDateTime.now());
        risk.setUpdateTime(LocalDateTime.now());
        this.riskMapper.insert(risk);

        RiskParamDO riskParam = new RiskParamDO();
        riskParam.setTenantId(request.getApplyId());
        riskParam.setApplyId(request.getApplyId());
        riskParam.setInputParam(JSONObject.toJSONString(request));
        riskParam.setOutputParam(StringUtils.EMPTY);
        riskParam.setCreateTime(LocalDateTime.now());
        riskParam.setUpdateTime(LocalDateTime.now());
        this.riskParamMapper.insert(riskParam);
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
        RiskDO risk = this.riskMapper.getRiskByTenantIdAndApplyId(varResult.getTenantId(), varResult.getApplyId());


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



        kieSession.addEventListener(agendaEventListener);

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
    public AuditResultVO getAuditResult(RiskDO audit) {
        AuditResultVO auditResult = new AuditResultVO();
        auditResult.setApplyId(audit.getApplyId());
        auditResult.setFlow(audit.getFlow());
        auditResult.setTenantId(audit.getTenantId());
        auditResult.setProductId(audit.getProductId());
        auditResult.setUserId(audit.getUserId());
        auditResult.setUserName(audit.getUserName());
        auditResult.setMobile(audit.getMobile());
        auditResult.setIdCard(audit.getIdCard());
        auditResult.setOrderId(audit.getOrderId());
        auditResult.setIp(audit.getIp());
        auditResult.setDeviceFingerPrint(audit.getDeviceFingerPrint());
        auditResult.setTransactionTime(audit.getTransactionTime());
        auditResult.setCallbackUrl(audit.getCallbackUrl());
        auditResult.setAuditCode(audit.getAuditCode());
        auditResult.setAuditScore(audit.getAuditScore());
        auditResult.setAuditStatus(audit.getAuditStatus());

        List<TriggeredRuleVO> triggeredRules = new ArrayList<>();
        List<TriggeredRuleDO> triggeredRuleList = this.triggeredRuleMapper.listTriggeredRuleLogByApplyId(audit.getApplyId());
        for(TriggeredRuleDO triggeredRuleDO : triggeredRuleList){
            RuleDO rule = this.ruleMapper.getRuleByCode(triggeredRuleDO.getRuleCode());
            TriggeredRuleVO triggeredRuleVO = new TriggeredRuleVO();
            triggeredRuleVO.setRulePackageCode(rule.getCode());
            triggeredRuleVO.setRuleCode(triggeredRuleDO.getRuleCode());
            triggeredRuleVO.setRuleName(rule.getName());
            triggeredRuleVO.setExpression(rule.getExpression());
            triggeredRuleVO.setParam(triggeredRuleDO.getParam());
            triggeredRuleVO.setTriggeredResult(rule.getResult());
            triggeredRules.add(triggeredRuleVO);
        }

        auditResult.setTriggeredRules(triggeredRules);
        return auditResult;
    }

}
