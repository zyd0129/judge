package com.ps.judge.provider.flow.service;

import com.ps.judge.provider.flow.ProcessDefinition;
import com.ps.judge.provider.flow.ProcessInstance;

public interface ProcessDefinitionService {
    ProcessDefinition getByCode(String processCode);

}
