package com.ts.judge.provider.service;

import com.ts.judge.provider.flow.ProcessInstance;

public interface ProcessInstanceService {
    void update(ProcessInstance processInstance);

    ProcessInstance getById(Integer id);

    void insert(ProcessInstance processInstance);

    void execute(ProcessInstance processInstance);
}
