package com.ps.judge.web.auth.service;

import com.ps.common.query.DepartmentQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.web.auth.objects.AuthDepartmentBO;

import java.util.List;

public interface DepartmentService {
    List<AuthDepartmentBO> query(QueryParams<DepartmentQuery> queryParams);

    void update(AuthDepartmentBO departmentBO);

    AuthDepartmentBO getById(int id);

    void deleteById(int id);

    void add(AuthDepartmentBO departmentBO);

    int total(QueryParams<DepartmentQuery> convertToQueryParam);

    List<AuthDepartmentBO> all();
}
