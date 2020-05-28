package com.ps.judge.provider.service.impl;

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
        auditTask.setTaskStatus(0);
        auditTask.setCallbackUrl(request.getCallbackUrl());
        auditTask.setGmtCreate(LocalDateTime.now());
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.insert(auditTask);

        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getTenantCode());
        auditTaskParam.setTaskId(auditTask.getId());
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(JSONObject.toJSONString(request));
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);

        ApiResponse applyResponse = this.juryApi.apply(request);
        if (!applyResponse.isSuccess()) {
            this.auditTaskMapper.updateTaskStatus(2, auditTask.getTenantCode(), auditTask.getApplyId());
            return ApiResponse.error(applyResponse.getCode(), applyResponse.getMessage());
        }
        this.auditTaskMapper.updateTaskStatus(1, auditTask.getTenantCode(), auditTask.getApplyId());
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public void startProcess(AuditTaskDO auditTask, VarResult varResult) {
        if(auditTask.getTaskStatus() != 1) {
            return;
        }
        this.auditTaskMapper.updateTaskStatus(3, auditTask.getTenantCode(), auditTask.getApplyId());

        String url = "http://192.168.159.129:8080/business-central/maven2" +
                "/com/ps/jury/api/objects/judge/1.0.0/judge-1.0.0.jar";
        KieServices kieServices = KieServices.Factory.get();
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
        Resource resource = kieServices.getResources().newInputStreamResource(is);
        KieModule kieModule = kieRepository.addKieModule(resource);
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        KieSession kieSession = kieContainer.newKieSession();

        Map<String, Object> map = new HashMap();
        List<AuditTaskTriggeredRuleDO> triggeredRuleList = new ArrayList<>();
        map.put("system", varResult.getSystem());
        map.put("merchant", varResult.getMerchant());
        map.put("product", varResult.getProduct());
        map.put("triggeredRuleList", triggeredRuleList);
        kieSession.startProcess("judge", map);
        kieSession.dispose();
        this.auditTaskMapper.updateTaskStatus(5, auditTask.getTenantCode(), auditTask.getApplyId());
        //todo 更新 task status 和 AuditCode AuditScore

        triggeredRuleList = (List<AuditTaskTriggeredRuleDO>) map.get("triggeredRuleList");
        List<TriggeredRuleVO> triggeredRuleVOList = new ArrayList<>();
        for(AuditTaskTriggeredRuleDO triggeredRule : triggeredRuleList){
            triggeredRule.setTaskId(auditTask.getId());
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
        auditResult.setTaskStatus(5);
        //auditResult.setAuditCode();
        //auditResult.setAuditScore();

        auditResult.setTriggeredRules(triggeredRuleVOList);
        ApiResponse apiResponse = ApiResponse.success(auditResult);
        this.auditTaskParamMapper.updateOutputRawParam(JSONObject.toJSONString(apiResponse), auditResult.getTenantCode(), auditResult.getApplyId());
        this.auditTaskMapper.updateTaskStatus(6, auditTask.getTenantCode(), auditTask.getApplyId());
        this.sendAuditResult(auditTask, apiResponse);
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditTaskDO auditTask) {
        AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
        if(StringUtils.isNotEmpty(auditTaskParam.getOutputRawParam())){
            return JSONObject.parseObject(auditTaskParam.getOutputRawParam(), new ApiResponse<AuditResultVO>().getClass());
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
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(2);
        if(auditTaskList.size() == 0){
            return;
        }
        for(AuditTaskDO auditTask : auditTaskList){
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
            String inputRawParam = auditTaskParam.getInputRawParam();
            ApplyRequest applyRequest = JSONObject.parseObject(inputRawParam, ApplyRequest.class);
            ApiResponse applyResponse = this.juryApi.apply(applyRequest);
            if (applyResponse.isSuccess()) {
                this.auditTaskMapper.updateTaskStatus(1, auditTask.getTenantCode(), auditTask.getApplyId());
            }
            this.auditTaskMapper.updateTaskStatus(2, auditTask.getTenantCode(), auditTask.getApplyId());
        }
    }

    @Override
    public void callbackTenant() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(6);
        if(auditTaskList.size() == 0) {
            return;
        }
        for(AuditTaskDO auditTask : auditTaskList) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper
                    .getAuditTaskParam(auditTask.getTenantCode(), auditTask.getApplyId());
            String outputRawParam = auditTaskParam.getOutputRawParam();
            ApiResponse<JSONObject> apiResponse = JSONObject.parseObject(outputRawParam, ApiResponse.class);
            AuditResultVO auditResultVO = JSONObject.toJavaObject(apiResponse.getData(), AuditResultVO.class);
            this.sendAuditResult(auditTask, ApiResponse.success(auditResultVO));
        }
    }

    private void sendAuditResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse){
        int callbackCount = auditTask.getCallbackCount();
        if(callbackCount > 3){
            return;
        }
        callbackCount++;
        this.auditTaskMapper.updateCallbackCount(callbackCount, auditTask.getTenantCode(), auditTask.getApplyId());

        //todo url修改成callbackUrl
        if(this.sendPost("http://localhost:8081/callback/accept", apiResponse)){
            this.auditTaskMapper.updateTaskStatus(7, auditTask.getTenantCode(), auditTask.getApplyId());
            return;
        }

        if(callbackCount == 3){
            this.auditTaskMapper.updateTaskStatus(8, auditTask.getTenantCode(), auditTask.getApplyId());
        }
    }

    private boolean sendPost(String url, ApiResponse<AuditResultVO> apiResponse){
        HttpEntity<ApiResponse<AuditResultVO>> requestEntity = new HttpEntity(apiResponse, httpHeaders);
        ResponseEntity<JSONObject> result = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
        if(result.getStatusCode() == HttpStatus.OK) {
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
