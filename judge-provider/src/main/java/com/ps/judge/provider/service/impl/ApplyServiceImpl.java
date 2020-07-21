package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.ApplyService;
import com.ps.judge.provider.task.AsyncProcessTask;
import com.ps.jury.api.JuryApi;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户申请阶段服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
@Service
public class ApplyServiceImpl implements ApplyService {
    private static final int MAX_RETRY_COUNT = 5;

    @Autowired
    JuryApi juryApi;
    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    AsyncProcessTask asyncProcessTask;

    @Override
    @Transactional
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

        String requestString = JSON.toJSONString(request, SerializerFeature.WriteMapNullValue);
        Integer auditId = auditTask.getId();
        AuditTaskParamDO auditTaskParam = new AuditTaskParamDO();
        auditTaskParam.setTenantCode(request.getTenantCode());
        auditTaskParam.setTaskId(auditId);
        auditTaskParam.setApplyId(request.getApplyId());
        auditTaskParam.setInputRawParam(requestString);
        auditTaskParam.setOutputRawParam(StringUtils.EMPTY);
        auditTaskParam.setVarResult(StringUtils.EMPTY);
        auditTaskParam.setGmtCreate(LocalDateTime.now());
        auditTaskParam.setGmtModified(LocalDateTime.now());
        this.auditTaskParamMapper.insert(auditTaskParam);
        this.asyncProcessTask.applyJury(auditTask, request);
        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(request.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(AuditTaskDO auditTask) {
        int auditId = auditTask.getId();

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            if (auditTask.getRetryCount() < MAX_RETRY_COUNT) {
                return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单自动重试中，不能手动重试");
            }
        }

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.VAR_COMPUTE_FAIL.getCode()
                || auditTask.getTaskStatus() == AuditTaskStatusEnum.FORWARDED_FAIL.getCode()) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            ApplyRequest request = JSON.parseObject(auditTaskParam.getInputRawParam(), ApplyRequest.class);
            this.asyncProcessTask.applyJury(auditTask, request);
        }

        if (auditTask.getTaskStatus() == AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode()) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            Map varResultMap = JSON.parseObject(auditTaskParam.getVarResult(), Map.class);
            this.asyncProcessTask.startProcess(auditTask, varResultMap);
        }

        ApplyResultVO applyResultVO = new ApplyResultVO();
        applyResultVO.setApplyId(auditTask.getApplyId());
        return ApiResponse.success(applyResultVO);
    }

    @Override
    public void reapplyJury() {
        List<AuditTaskDO> auditTaskList = new ArrayList<>();
        List<AuditTaskDO> requestReceivedList = this.auditTaskMapper
                .listAuditTaskByTaskStatusAndRetryCount(AuditTaskStatusEnum.REQUEST_RECEIVED.getCode(), MAX_RETRY_COUNT);
        List<AuditTaskDO> forwardedFailList = this.auditTaskMapper
                .listAuditTaskByTaskStatusAndRetryCount(AuditTaskStatusEnum.FORWARDED_FAIL.getCode(), MAX_RETRY_COUNT);
        auditTaskList.addAll(requestReceivedList);
        auditTaskList.addAll(forwardedFailList);
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            Integer auditId = auditTask.getId();
            Integer retryCount = auditTask.getRetryCount() + 1;
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditId);
            String inputRawParam = auditTaskParam.getInputRawParam();
            ApplyRequest applyRequest = JSON.parseObject(inputRawParam, ApplyRequest.class);
            ApiResponse<String> applyResponse = this.juryApi.apply(applyRequest);
            if (applyResponse.isSuccess()) {
                auditTask.setTaskStatus(AuditTaskStatusEnum.FORWARDED_SUCCESS.getCode());
            } else {
                auditTask.setTaskStatus(AuditTaskStatusEnum.FORWARDED_FAIL.getCode());
            }
            auditTask.setRetryCount(retryCount);
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
        }
    }
}
