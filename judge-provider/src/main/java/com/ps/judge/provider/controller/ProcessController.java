package com.ps.judge.provider.controller;

import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.judge.provider.service.ConfigFlowService;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ProcessController implements JudgeApi {
    @Autowired
    ProcessService processService;
    @Autowired
    ConfigFlowService configFlowService;

    @Override
    public ApiResponse<ApplyResultVO> applyAudit(ApplyRequest applyRequest) {
        if (StringUtils.isEmpty(applyRequest.getApplyId())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "ApplyId 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getFlowCode())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "FlowCode 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getTenantCode())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "TenantCode 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getProductCode())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "ProductCode 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getCallbackUrl())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "CallbackUrl 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getUserId())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "UserId 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getUserName())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "UserName 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getMobile())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Mobile 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getUserName())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "UserName 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getIdCard())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "IdCard 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getOrderId())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "OrderId 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getIp())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Ip 不能为空");
        }
        if (StringUtils.isEmpty(applyRequest.getDeviceFingerPrint())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "DeviceFingerPrint 不能为空");
        }
        if (Objects.isNull(applyRequest.getTransactionTime())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "TransactionTime 不能为空");
        }
        AuditTaskDO audit = this.processService.getAuditTask(applyRequest.getTenantCode(), applyRequest.getApplyId());
        if (Objects.nonNull(audit)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "申请订单已存在");
        }
        ConfigFlowDO configFlow = this.configFlowService.getByFlowCode(applyRequest.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (configFlow.getStatus() != StatusEnum.ENABLE.status()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流未启用");
        }
        return this.processService.apply(applyRequest);
    }

    @Override
    public ApiResponse<ApplyResultVO> retryAudit(Integer taskId) {
        if (Objects.isNull(taskId)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }
        if (taskId <= 0) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
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
    public ApiResponse<String> loadFlow(String flowCode, boolean load) {
        if (StringUtils.isEmpty(flowCode)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "flowCode不能为空");
        }
        ConfigFlowDO configFlow = this.configFlowService.getByFlowCode(flowCode);
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        if (load) {
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
        if (!apiResponse.isSuccess()) {
            this.processService.updateAuditStatus(auditTask.getId(), AuditTaskStatusEnum.VAR_COMPUTE_FAIL.getCode());
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "变量计算失败");
        }
        return this.processService.saveVarResult(auditTask, varResult);
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditResultQuery auditResultQuery) {
        String tenantCode = auditResultQuery.getTenantCode();
        String applyId = auditResultQuery.getApplyId();
        if (StringUtils.isEmpty(tenantCode)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "TenantCode 不能为空");
        }
        if (StringUtils.isEmpty(applyId)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "ApplyId 不能为空");
        }
        AuditTaskDO auditTask = this.processService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "订单不存在");
        }
        return this.processService.getAuditResult(auditTask);
    }

}
