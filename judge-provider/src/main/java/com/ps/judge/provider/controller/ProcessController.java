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
import com.ps.judge.provider.service.ConfigFlowService;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ProcessController implements JudgeApi {
    @Autowired
    ProcessService processService;
    @Autowired
    ConfigFlowService configFlowService;

    @Override
    public ApiResponse<ApplyResultVO> applyAudit(@Validated ApplyRequest applyRequest) {
        AuditTaskDO audit = this.processService.getAuditTask(applyRequest.getTenantCode(), applyRequest.getApplyId());
        if (Objects.nonNull(audit)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "申请订单已存在");
        }
        ConfigFlowDO configFlow = this.configFlowService.getByFlowCode(applyRequest.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.getStatus()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流未启用");
        }
        return this.processService.apply(applyRequest);
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(Integer taskId) {
        if (Objects.isNull(taskId)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "taskId 不能为空");
        }
        if (taskId <= 0) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "taskId 不能小于等于0");
        }
        AuditTaskDO auditTask = this.processService.getAuditTask(taskId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }
        if (auditTask.getTaskStatus() != AuditTaskStatusEnum.VAR_COMPUTE_FAIL.getCode()
                && auditTask.getTaskStatus() != AuditTaskStatusEnum.AUDIT_COMPLETE_FAIL.getCode()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单未失败，不能重试");
        }
        return this.processService.retryAudit(auditTask);
    }

    @Override
    public ApiResponse<String> loadFlow(@Validated LoadFlowVO loadFlowVO) {
        ConfigFlowDO configFlow = this.configFlowService.getByFlowCode(loadFlowVO.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (loadFlowVO.isLoad()) {
            if (this.configFlowService.loadFlow(configFlow)) {
                return ApiResponse.success();
            }
        } else {
            if (this.configFlowService.unLoadFlow(configFlow)) {
                return ApiResponse.success();
            }
        }
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流加载失败");
    }

    @Override
    public ApiResponse<String> submitVar(ApiResponse<VarResult> apiResponse) {
        VarResult varResult = apiResponse.getData();
        if (Objects.isNull(varResult)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "信息不存在");
        }
        String tenantCode = varResult.getTenantCode();
        String applyId = varResult.getApplyId();
        AuditTaskDO auditTask = this.processService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }
        if (!StringUtils.equals(auditTask.getFlowCode(), varResult.getFlowCode())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不一致");
        }
        if (!apiResponse.isSuccess()) {
            this.processService.updateAuditStatus(AuditTaskStatusEnum.VAR_COMPUTE_FAIL.getCode(), auditTask.getId());
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "变量计算失败");
        }
        return this.processService.saveVarResult(auditTask, varResult);
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(@Validated AuditResultQuery auditResultQuery) {
        String tenantCode = auditResultQuery.getTenantCode();
        String applyId = auditResultQuery.getApplyId();
        AuditTaskDO auditTask = this.processService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "订单不存在");
        }
        return this.processService.getAuditResult(auditTask);
    }

}
