package com.ps.judge.web.auth;

import com.ps.judge.web.auth.objects.AuthDepartmentBO;

import java.util.List;

public interface DepartmentService {
    List<AuthDepartmentBO> query();

    void update(AuthDepartmentBO departmentBO);
}
