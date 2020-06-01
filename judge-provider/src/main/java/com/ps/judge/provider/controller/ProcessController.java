package com.ps.judge.provider.controller;

import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.service.ConfigFlowService;
import com.ps.judge.provider.service.ProcessService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ProcessController extends BaseController implements JudgeApi {
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
        ConfigFlowBO configFlow = this.configFlowService.getByFlowCode(applyRequest.getFlowCode());
        if (Objects.isNull(configFlow)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "规则流不存在");
        }
        return this.processService.apply(applyRequest);
    }

    @Override
    public ApiResponse<String> submitVar(ApiResponse<VarResult> apiResponse) {
        if (!apiResponse.isSuccess()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "变量计算失败");
        }
        VarResult varResult = apiResponse.getData();
        String tenantCode = apiResponse.getData().getTenantCode();
        String applyId = apiResponse.getData().getApplyId();
        AuditTaskDO auditTask = this.processService.getAuditTask(tenantCode, applyId);
        if (Objects.isNull(auditTask)) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }
        ApiResponse response = this.processService.saveVarResult(auditTask, varResult);
        return response;
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

    @PostMapping("/callback/accept")
    public ApiResponse accept(@RequestBody ApiResponse<AuditResultVO> apiResponse) {
        System.out.println("callback " + apiResponse);
        AuditResultVO auditResultVO = apiResponse.getData();
        // TODO: 编写相应的业务逻辑即可
        return ApiResponse.success();
    }

}
