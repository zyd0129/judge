package com.ps.judge.web.service.impl;

import com.ps.common.enums.TaskStatus;
import com.ps.common.exception.BizException;
import com.ps.common.query.QueryParams;
import com.ps.common.query.TaskQuery;
import com.ps.judge.api.JudgeApi;
import com.ps.judge.api.entity.ApplyResultVO;
import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.mapper.AuditTaskMapper;
import com.ps.judge.web.models.AuditTaskBO;
import com.ps.judge.web.service.AuditTaskService;
import com.ps.jury.api.common.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AuditTaskServiceImpl implements AuditTaskService {
    private List<Integer> successStatus = Arrays.asList(6, 8, 9, 10);
    private List<Integer> runningStatus = Arrays.asList(0,1,2,3,4,5);
    List<Integer> failureStatus = Collections.singletonList(7);

    @Autowired
    AuditTaskMapper auditTaskMapper;

    @Autowired
    JudgeApi judgeApi;
    
    @Override
    public List<AuditTaskBO> query(QueryParams<TaskQuery> queryParams) {
        processQueryParams(queryParams);
        return convertToBOList(auditTaskMapper.query(queryParams));
    }

    @Override
    public int count(QueryParams<TaskQuery> queryParams) {
        processQueryParams(queryParams);
        return auditTaskMapper.count(queryParams);
    }

    @Override
    public void revoke(int taskId) {
        ApiResponse<ApplyResultVO> response = judgeApi.retryAudit(taskId);
        if (!response.isSuccess()) {
            throw new BizException(60001, "重掉失败");
        }
    }

    private AuditTaskBO convertToBO(AuditTaskDO auditTaskDO) {
        if (auditTaskDO == null) {
            return null;
        }
        AuditTaskBO auditTaskBO = new AuditTaskBO();
        BeanUtils.copyProperties(auditTaskDO, auditTaskBO);
        auditTaskBO.setTaskStatus(convertToTaskStatus(auditTaskDO.getTaskStatus()));
        return auditTaskBO;
    }

    /**
     * 章谦(Ethan) 6-12 下午01:57:43
     * 0 请求已接收 1 请求转发成功 2 请求转发失败  3 变量接受成功  4, 变量计算失败 5 审核中
     *
     * 章谦(Ethan) 6-12 下午01:58:05
     * 查询中  0 请求已接收 1 请求转发成功 2 请求转发失败  3 变量接受成功  4, 变量计算失败 5 审核中
     *
     * 章谦(Ethan) 6-12 下午01:58:28
     * 失败  7 审核失败
     *
     * 章谦(Ethan) 6-12 下午01:58:48
     * 成功  6 审核完成  8 审核完成回调中 9 回调成功 10 回调失败
     * @param taskStatus
     * @return
     */

    private TaskStatus convertToTaskStatus(Integer taskStatus) {

        if (successStatus.contains(taskStatus)) {
            return TaskStatus.SUCCESS;
        }
        if (runningStatus.contains(taskStatus)) {
            return TaskStatus.RUNNING;
        }
        if (failureStatus.contains(taskStatus)) {
            return TaskStatus.FAILURE;
        }
        return null;
    }

    private List<AuditTaskBO> convertToBOList(List<AuditTaskDO> auditTaskDOList) {
        if (auditTaskDOList == null) {
            return null;
        }
        List<AuditTaskBO> auditTaskBOList = new ArrayList<>();
        for (AuditTaskDO auditTaskDO : auditTaskDOList) {
            AuditTaskBO auditTaskBO = convertToBO(auditTaskDO);
            auditTaskBOList.add(auditTaskBO);
        }
        return auditTaskBOList;
    }

    /**
     *  private String tenantCode;
     *     private String userName;
     *     private String mobile;
     *     private String idCard;
     * @param queryParams
     */
    private void processQueryParams(QueryParams<TaskQuery> queryParams) {
        if (queryParams == null || queryParams.getQuery() == null)
            return;

        String tenantCode = queryParams.getQuery().getTenantCode();
        String userName = queryParams.getQuery().getUserName();
        String mobile = queryParams.getQuery().getMobile();
        String idCard = queryParams.getQuery().getIdCard();
        TaskStatus status = queryParams.getQuery().getStatus();
        boolean fuzzy =false;
        if (!StringUtils.isEmpty(tenantCode)) {
            queryParams.getQuery().setTenantCode("%" + tenantCode + "%");
            fuzzy =true;
        }
        if (!StringUtils.isEmpty(userName)) {
            queryParams.getQuery().setUserName("%" + userName + "%");
            fuzzy =true;
        }
        if (!StringUtils.isEmpty(mobile)) {
            queryParams.getQuery().setMobile("%" + mobile + "%");
            fuzzy =true;
        }
        if (!StringUtils.isEmpty(idCard)) {
            queryParams.getQuery().setIdCard("%" + idCard + "%");
            fuzzy =true;
        }
        queryParams.getQuery().setFuzzy(fuzzy);
        if (TaskStatus.SUCCESS.equals(status)) {
            queryParams.getQuery().setTaskStatus(successStatus);
        } else if (TaskStatus.FAILURE.equals(status)) {
            queryParams.getQuery().setTaskStatus(failureStatus);
        } else if (TaskStatus.RUNNING.equals(status)) {
            queryParams.getQuery().setTaskStatus(runningStatus);
        } else {
            queryParams.getQuery().setTaskStatus(null);
        }
    }
}
