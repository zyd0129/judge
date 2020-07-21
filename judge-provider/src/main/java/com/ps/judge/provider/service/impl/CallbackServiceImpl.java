package com.ps.judge.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.provider.enums.AuditTaskStatusEnum;
import com.ps.judge.provider.service.CallbackService;
import com.ps.jury.api.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 商户回调
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
@Service
public class CallbackServiceImpl implements CallbackService {
    private static final int MAX_CALLBACK_COUNT = 3;

    @Autowired
    AuditTaskMapper auditTaskMapper;
    @Autowired
    AuditTaskParamMapper auditTaskParamMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HttpHeaders httpHeaders;

    @Override
    public void sendAuditTaskResult(AuditTaskDO auditTask, ApiResponse<AuditResultVO> apiResponse) {
        int callbackCount = auditTask.getCallbackCount();
        if (callbackCount >= MAX_CALLBACK_COUNT) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK_FAIL.getCode());
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
            return;
        }
        auditTask.setCallbackCount(++callbackCount);
        auditTask.setGmtModified(LocalDateTime.now());
        this.auditTaskMapper.update(auditTask);
        if (this.sendPost(auditTask.getCallbackUrl(), apiResponse)) {
            auditTask.setTaskStatus(AuditTaskStatusEnum.CALLBACK_SUCCESS.getCode());
            auditTask.setGmtModified(LocalDateTime.now());
            this.auditTaskMapper.update(auditTask);
        }
    }

    private boolean sendPost(String url, ApiResponse<AuditResultVO> apiResponse) {
        HttpEntity<ApiResponse<AuditResultVO>> requestEntity = new HttpEntity<>(apiResponse, this.httpHeaders);
        ResponseEntity<JSONObject> result = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
        if (result.getStatusCode() == HttpStatus.OK) {
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

    @Override
    public void callbackTenant() {
        List<AuditTaskDO> auditTaskList = this.auditTaskMapper.listAuditTaskByTaskStatus(AuditTaskStatusEnum.CALLBACK.getCode());
        if (auditTaskList.isEmpty()) {
            return;
        }
        for (AuditTaskDO auditTask : auditTaskList) {
            AuditTaskParamDO auditTaskParam = this.auditTaskParamMapper.getAuditTaskParam(auditTask.getId());
            String outputRawParam = auditTaskParam.getOutputRawParam();
            ApiResponse<JSONObject> apiResponse = JSON.parseObject(outputRawParam, ApiResponse.class);
            AuditResultVO auditResultVO = JSON.toJavaObject(apiResponse.getData(), AuditResultVO.class);
            this.sendAuditTaskResult(auditTask, ApiResponse.success(auditResultVO));
        }
    }
}
