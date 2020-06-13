package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.query.FlowQuery;
import com.ps.common.query.QueryVo;
import com.ps.common.query.TaskQuery;
import com.ps.judge.web.models.AuditTaskBO;
import com.ps.judge.web.models.ConfigFlowBO;
import com.ps.judge.web.service.AuditTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager/flow/tasks")
public class AuditTaskController {
    @Autowired
    AuditTaskService taskService;

    @PostMapping(value = "query", params = "query=true")
    public ApiResponse<PageResult<AuditTaskBO>> queryTask(@RequestBody QueryVo<TaskQuery> taskQuery) {
        List<AuditTaskBO> all = taskService.query(taskQuery.convertToQueryParam());
        int total = taskService.count(taskQuery.convertToQueryParam());
        PageResult<AuditTaskBO> pageResult = new PageResult<>();
        pageResult.setCurPage(taskQuery.getCurPage());
        pageResult.setPageSize(taskQuery.getPageSize());
        pageResult.setTotal(total);
        pageResult.setData(all);
        return ApiResponse.success(pageResult);
    }

    @PostMapping(value = "query", params = "query=false")
    public ApiResponse<PageResult<AuditTaskBO>> listFlow(@RequestBody QueryVo<TaskQuery> taskQuery) {
        taskQuery.setQuery(null);
        return queryTask(taskQuery);
    }
}
