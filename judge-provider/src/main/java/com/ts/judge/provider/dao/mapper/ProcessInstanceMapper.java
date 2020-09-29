package com.ts.judge.provider.dao.mapper;

import com.ts.judge.provider.dao.entity.ProcessInstanceDO;

public interface ProcessInstanceMapper {
    void insert(ProcessInstanceDO processInstanceDO);

    ProcessInstanceDO getById(Integer id);

    void update(ProcessInstanceDO processInstanceDO);
}
