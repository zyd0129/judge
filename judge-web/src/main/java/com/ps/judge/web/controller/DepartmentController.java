package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.judge.web.auth.DepartmentService;
import com.ps.judge.web.auth.objects.AuthDepartmentBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/auth")
@RestController
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @GetMapping("/departments/list")
    public ApiResponse<List<AuthDepartmentBO>> departmentList() {
       List<AuthDepartmentBO> departmentBOList = departmentService.query();
        return ApiResponse.success(departmentBOList);
    }


    @PostMapping("/departments/modify")
    public ApiResponse departmentList(@RequestBody AuthDepartmentBO departmentBO) {
        AuthUserBO currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        departmentBO.setGmtModified(LocalDateTime.now());
        departmentBO.setOperator(currentUser.getUsername());
        departmentService.update(departmentBO);
        return ApiResponse.success();
    }

}
