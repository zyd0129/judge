package com.ps.judge.provider.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.provider.dao.entity.ProcessDefinitionDO;
import com.ps.judge.provider.dao.mapper.ProcessDefinitionMapper;
import com.ps.judge.provider.flow.ProcessDefinition;
import com.ps.judge.provider.flow.bpmn.SimpleBPMDefinition;
import com.ps.judge.provider.flow.service.ProcessDefinitionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {
    @Autowired
    ProcessDefinitionMapper processDefinitionMapper;

    @Override
    public ProcessDefinition getByCode(String processCode) {
        ProcessDefinitionDO processDefinitionDO = processDefinitionMapper.getByCode(processCode);

        return convertToBO(processDefinitionDO);
    }

    private ProcessDefinition convertToBO(ProcessDefinitionDO processDefinitionDO) {
        if (processDefinitionDO == null) {
            return null;
        }
        ProcessDefinition processDefinition = new ProcessDefinition();
        BeanUtils.copyProperties(processDefinitionDO, processDefinition);
        processDefinition.setDefinition(JSONObject.parseObject(processDefinitionDO.getDefinition(), SimpleBPMDefinition.class));
        processDefinition.setGmtCreated(processDefinitionDO.getGmtCreated().toLocalDateTime());
        processDefinition.setGmtModified(processDefinitionDO.getGmtModified().toLocalDateTime());
        return processDefinition;
    }
}
