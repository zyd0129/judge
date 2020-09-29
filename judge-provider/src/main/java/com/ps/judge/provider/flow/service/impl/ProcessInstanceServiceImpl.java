package com.ps.judge.provider.flow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.provider.dao.entity.ProcessInstanceDO;
import com.ps.judge.provider.dao.mapper.ProcessInstanceMapper;
import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.node.runtime.NodeInstance;
import com.ps.judge.provider.flow.service.ProcessInstanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessInstanceServiceImpl implements ProcessInstanceService {
    @Autowired
    ProcessInstanceMapper processInstanceMapper;

    @Override
    public void update(ProcessInstance processInstance) {

    }

    @Override
    public ProcessInstance getById(Integer id) {
        ProcessInstanceDO processInstanceDO = processInstanceMapper.getById(id);
        return convertToBO(processInstanceDO);
    }


    @Override
    public void insert(ProcessInstance processInstance) {
        ProcessInstanceDO processInstanceDO = convertToDO(processInstance);
        processInstanceMapper.insert(processInstanceDO);
        processInstanceDO.setId(processInstanceDO.getId());
    }


    private ProcessInstanceDO convertToDO(ProcessInstance processInstance) {
        if (processInstance == null) {
            return null;
        }
        ProcessInstanceDO processInstanceDO = new ProcessInstanceDO();
        BeanUtils.copyProperties(processInstance, processInstanceDO);

        processInstanceDO.setDefinition(JSONObject.toJSONString(processInstance.getNodeList()));
        processInstanceDO.setApplyRequest(JSONObject.toJSONString(processInstance.getApplyRequest()));
        processInstanceDO.setProcessParams(JSONObject.toJSONString(processInstance.getProcessParams()));
        NodeInstance currentNodeInstance = processInstance.getCurrentNodeInstance();
        if (currentNodeInstance != null) {
            processInstanceDO.setCurrentNodeName(currentNodeInstance.getName());
            processInstanceDO.setCurrentNodeStatus(currentNodeInstance.getStatus().name());
        }
        processInstanceDO.setStatus(processInstance.getStatus().name());

        return processInstanceDO;
    }

    private ProcessInstance convertToBO(ProcessInstanceDO processInstanceDO) {
        if (processInstanceDO == null) {
            return null;
        }
        ProcessInstance processInstance = new ProcessInstance();
        BeanUtils.copyProperties(processInstanceDO, processInstance);
        return processInstance;
    }
}
