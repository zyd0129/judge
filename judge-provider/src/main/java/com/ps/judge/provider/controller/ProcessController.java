package com.ps.judge.provider.controller;

import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.api.entity.LoadFlowVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.judge.provider.service.ApplyService;
import com.ps.judge.provider.service.AuditTaskService;
import com.ps.judge.provider.service.FlowService;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class ProcessController implements JudgeApi {
    @Autowired
    AuditTaskService auditTaskService;
    @Autowired
    FlowService flowService;
    @Autowired
    ApplyService applyService;
    @Autowired
    ProcessService processService;

    @Override
    public ApiResponse<ApplyResultVO> applyAudit(@Validated ApplyRequest applyRequest) {
        AuditTaskDO audit = this.auditTaskService.getAuditTask(applyRequest.getTenantCode(), applyRequest.getApplyId());
        if (Objects.nonNull(audit)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "申请订单已存在");
        }
        ConfigFlowDO configFlow = this.flowService.getByFlowCode(applyRequest.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.getStatus()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流未启用");
        }
        return this.applyService.apply(applyRequest);
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(Integer taskId) {
        if (Objects.isNull(taskId)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "taskId 不能为空");
        }
        if (taskId <= 0) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "taskId 不能小于等于0");
        }
        AuditTaskDO auditTask = this.auditTaskService.getAuditTask(taskId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.VAR_COMPUTE_FAIL.value()
                && auditTask.getTaskStatus() != AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.value()
                && auditTask.getTaskStatus() != AuditTaskStatusEnum.FORWARDED_FAIL.value()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单未失败，不能重试");
        }
        return this.applyService.retryAudit(auditTask);
    }

    @Override
    public ApiResponse<String> loadFlow(@Validated LoadFlowVO loadFlowVO) {
        ConfigFlowDO configFlow = this.flowService.getByFlowCode(loadFlowVO.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (loadFlowVO.isLoad()) {
            if (this.flowService.loadFlow(configFlow)) {
                return ApiResponse.success();
            }
        } else {
            if (this.flowService.removeFlow(configFlow)) {
                return ApiResponse.success();
            }
        }
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流加载失败");
    }

    @Override
    public ApiResponse<String> submitVar(ApiResponse<Map> apiResponse) {
        Map varResultMap = apiResponse.getData();
        if (varResultMap.isEmpty()) {
            return ApiResponse.success("varResult 信息不存在");
        }
        String tenantCode = (String) varResultMap.get("tenantCode");
        String applyId =  (String) varResultMap.get("applyId");
        AuditTaskDO auditTask = this.auditTaskService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.success("订单不存在");
        }
        if (!StringUtils.equals(auditTask.getFlowCode(), (String) varResultMap.get("flowCode"))) {
            return ApiResponse.success("规则流不一致");
        }
        if (!apiResponse.isSuccess()) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.VAR_COMPUTE_FAIL.value());
            this.auditTaskService.updateAuditTask(auditTask);
            return ApiResponse.success("变量计算失败");
        }
        return this.processService.saveVarResult(auditTask, varResultMap);
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(@Validated AuditResultQuery auditResultQuery) {
        String tenantCode = auditResultQuery.getTenantCode();
        String applyId = auditResultQuery.getApplyId();
        AuditTaskDO auditTask = this.auditTaskService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "订单不存在");
        }
        return this.auditTaskService.getAuditResult(auditTask);
    }
}