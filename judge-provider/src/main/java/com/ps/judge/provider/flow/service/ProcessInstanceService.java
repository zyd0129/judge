package com.ps.judge.provider.flow.service;

import com.ps.judge.provider.flow.ProcessInstance;

public interface ProcessInstanceService {
    void update(ProcessInstance processInstance);

    ProcessInstance getById(Integer id);

    void insert(ProcessInstance processInstance);
}
