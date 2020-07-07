package com.ps.judge.dao.mapper;

import com.ps.common.query.DepartmentQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DepartmentMapper {
    List<AuthDepartmentDO> query(QueryParams<DepartmentQuery> q);

    AuthDepartmentDO getById(int id);

    int count(QueryParams<DepartmentQuery> q);

    List<AuthDepartmentDO> all();

    int insert(AuthDepartmentDO authDepartmentDO);

    void update(AuthDepartmentDO authDepartmentDO);

    int delete(int id);
}
