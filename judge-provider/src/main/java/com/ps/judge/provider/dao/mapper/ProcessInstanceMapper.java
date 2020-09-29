package com.ps.judge.provider.dao.mapper;

import com.ps.judge.provider.dao.entity.ProcessInstanceDO;

public interface ProcessInstanceMapper {
    void insert(ProcessInstanceDO processInstanceDO);

    ProcessInstanceDO getById(Integer id);
}
