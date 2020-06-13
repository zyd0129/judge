package com.ps.judge.web.service;


import com.ps.common.query.QueryParams;
import com.ps.common.query.TaskQuery;
import com.ps.judge.web.models.AuditTaskBO;

import java.util.List;

public interface AuditTaskService {
    List<AuditTaskBO> query(QueryParams<TaskQuery> queryParams);

    int count(QueryParams<TaskQuery> queryParams);
}
