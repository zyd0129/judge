package com.ps.judge.provider.controller;

import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.api.entity.AuditResultQuery;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.provider.service.JudgeService;
import com.ps.jury.api.common.ApiResponse;
import com.ps.jury.api.request.ApplyRequest;
import com.ps.jury.api.response.VarResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class JudgeController extends BaseController implements JudgeApi {
    @Autowired
    JudgeService judgeService;

    @Override
    public ApiResponse<ApplyResultVO> applyAudit(ApplyRequest applyRequest) {
        if(StringUtils.isEmpty(applyRequest.getApplyId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getFlow())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getTenantId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getProductId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getCallbackUrl())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserName())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getMobile())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserName())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserName())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserName())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getUserName())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getIdCard())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getOrderId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getOrderId())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getIp())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(StringUtils.isEmpty(applyRequest.getDeviceFingerPrint())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        if(Objects.isNull(applyRequest.getTransactionTime())){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        AuditTaskDO audit = this.judgeService.getAuditByTenantIdAndApplyId(applyRequest.getTenantId(), applyRequest.getApplyId());
        if (Objects.nonNull(audit)){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }
        return this.judgeService.apply(applyRequest);
    }

    @Override
    public ApiResponse<String> submitVar(ApiResponse<VarResult> apiResponse) {

        System.out.println(apiResponse);
        if(!apiResponse.isSuccess()){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        VarResult varResult = apiResponse.getData();
        // TODO: 2020/5/19 删除进件
        // TODO: 2020/5/19 缓存出件
        this.judgeService.startProcess(varResult);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<AuditResultVO> getAuditResult(AuditResultQuery auditResultQuery) {
        String tenantId = auditResultQuery.getTenantId();
        String applyId = auditResultQuery.getApplyId();

        if(StringUtils.isEmpty(tenantId)){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }
        if(StringUtils.isEmpty(applyId)){
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "");
        }

        AuditTaskDO audit = this.judgeService.getAuditByTenantIdAndApplyId(tenantId, applyId);
        if (Objects.isNull(audit)){
            return ApiResponse.error(HttpStatus.NOT_FOUND.value(), "");
        }

        return ApiResponse.success(this.judgeService.getAuditResult(audit));
    }

}
