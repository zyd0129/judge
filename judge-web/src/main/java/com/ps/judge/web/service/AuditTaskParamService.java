package com.ps.judge.web.service;


import com.ps.common.query.QueryParams;
import com.ps.common.query.TaskQuery;
import com.ps.judge.web.models.AuditTaskBO;
import com.ps.judge.web.models.AuditTaskParamBO;

import java.util.List;

public interface AuditTaskParamService {
    AuditTaskParamBO getById(int id);
}
