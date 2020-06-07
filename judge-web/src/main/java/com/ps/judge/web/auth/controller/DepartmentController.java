package com.ps.judge.web.auth.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.query.QueryVo;
import com.ps.judge.web.auth.service.DepartmentService;
import com.ps.judge.web.auth.objects.AuthDepartmentBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/auth")
@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping("/departments/list")
    @PreAuthorize("hasAuthority('department_list')")
    public ApiResponse<PageResult<AuthDepartmentBO>> departmentList(@RequestBody QueryVo<AuthDepartmentBO> queryVo) {
        queryVo.setQuery(null);
        List<AuthDepartmentBO> departmentBOList = departmentService.query(queryVo.convertToQueryParam());
        PageResult<AuthDepartmentBO> pageResult = new PageResult<>(queryVo.getCurPage(), queryVo.getPageSize(), departmentBOList);
        return ApiResponse.success(pageResult);
    }

    @PostMapping("/departments/query")
    @PreAuthorize("hasAuthority('department_query')")
    public ApiResponse<PageResult<AuthDepartmentBO>> departmentQuery(@RequestBody QueryVo<AuthDepartmentBO> queryVo) {
        List<AuthDepartmentBO> departmentBOList = departmentService.query(queryVo.convertToQueryParam());
        PageResult<AuthDepartmentBO> pageResult = new PageResult<>(queryVo.getCurPage(), queryVo.getPageSize(), departmentBOList);
        return ApiResponse.success(pageResult);
    }

    @PostMapping("/departments/modify")
    @PreAuthorize("hasAuthority('department_modify')")
    public ApiResponse departmentList(@RequestBody AuthDepartmentBO departmentBO) {
        AuthUserBO currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        departmentBO.setGmtModified(LocalDateTime.now());
        departmentBO.setOperator(currentUser.getUsername());
        departmentService.update(departmentBO);
        return ApiResponse.success();
    }

    @PostMapping("/departments/add")
    @PreAuthorize("hasAuthority('department_add')")
    public ApiResponse addDepartment(@RequestBody AuthDepartmentBO departmentBO) {
        AuthUserBO currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        departmentBO.setGmtCreated(LocalDateTime.now());
        departmentBO.setGmtModified(LocalDateTime.now());
        departmentBO.setOperator(currentUser.getUsername());
        departmentService.add(departmentBO);
        return ApiResponse.success();
    }

    @PostMapping("/departments/delete")
    @PreAuthorize("hasAuthority('department_delete')")
    public ApiResponse deleteDepartment(@RequestParam int id) {
        departmentService.deleteById(id);
        return ApiResponse.success();
    }

}
