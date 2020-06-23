package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.query.QueryParams;
import com.ps.common.query.QueryVo;
import com.ps.common.query.TaskQuery;
import com.ps.judge.web.models.AuditTaskBO;
import com.ps.judge.web.models.AuditTaskParamBO;
import com.ps.judge.web.service.AuditTaskParamService;
import com.ps.judge.web.service.AuditTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager/flow/instances")
public class AuditTaskController {
    @Autowired
    AuditTaskService taskService;
    @Autowired
    AuditTaskParamService paramService;

    @PostMapping(value = "query", params = "query=true")
    public ApiResponse<PageResult<AuditTaskBO>> queryTask(@RequestBody QueryVo<TaskQuery> taskQuery) {
        QueryParams<TaskQuery> queryParams = taskQuery.convertToQueryParam();
        List<AuditTaskBO> all = taskService.query(queryParams);
        int total = taskService.count(queryParams);
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

    @PostMapping(value = "/restart")
    public ApiResponse revoke(@RequestParam int taskId) {
        taskService.revoke(taskId);
        return ApiResponse.success();
    }

    @GetMapping(value = "/result")
    public ApiResponse<AuditTaskParamBO> taskParams(@RequestParam int taskId) {

        return ApiResponse.success(paramService.getById(taskId));
    }
}
