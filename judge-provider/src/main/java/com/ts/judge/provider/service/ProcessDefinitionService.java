package com.ts.judge.provider.service;

import com.ts.judge.provider.flow.ProcessDefinition;

public interface ProcessDefinitionService {
    ProcessDefinition getByCode(String processCode);

}
