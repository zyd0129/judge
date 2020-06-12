package com.ps.judge.web.service;


import com.ps.common.ApiResponse;
import com.ps.common.exception.BizException;
import com.ps.common.query.FlowQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.web.models.ConfigFlowBO;

import java.util.List;

public interface ConfigFlowService {
    List<ConfigFlowBO> getAll();

    List<ConfigFlowBO> query(QueryParams<FlowQuery> queryParams);

    ConfigFlowBO getByFlowCode(String flowCode);

    ConfigFlowBO getById(int id);

    void insert(ConfigFlowBO configFlowBO);

    void updateStatus(ConfigFlowBO configFlowBO) throws BizException;

    void delete(int id);

    int count(QueryParams<FlowQuery> queryParams);

    void modify(ConfigFlowBO configFlowBO);
}
