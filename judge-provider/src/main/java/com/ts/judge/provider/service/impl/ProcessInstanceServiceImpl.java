package com.ts.judge.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ts.judge.provider.dao.entity.ProcessInstanceDO;
import com.ts.judge.provider.dao.mapper.ProcessInstanceMapper;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.enums.RunTimeStatus;
import com.ts.judge.provider.flow.bpmn.SimpleBPMDefinition;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.service.ProcessInstanceService;
import com.ts.jury.api.request.ApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
    @Autowired
    ProcessInstanceMapper processInstanceMapper;

    @Autowired
    NodeInstanceService nodeInstanceService;

    @Override
    public void update(ProcessInstance processInstance) {
        ProcessInstanceDO processInstanceDO = convertToDO(processInstance);
        processInstanceMapper.update(processInstanceDO);
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
        processInstance.setId(processInstanceDO.getId());
    }

    /**
     * 主要是决定下一个节点任务,目前不支持任务重掉，从出现异常的任务开始执行
     *
     * @param processInstance
     */
    public void execute(ProcessInstance processInstance) {
        NodeInstance currentNodeInstance = processInstance.getCurrentNodeInstance();
        SimpleBPMDefinition definition = processInstance.getDefinition();
        if (currentNodeInstance == null) {
            if (processInstance.getStatus().equals(RunTimeStatus.CREATED)) {
                currentNodeInstance = definition.getStartNodeInstance();
                if (currentNodeInstance == null) {
                    log.error("process definition {} has no start node", processInstance.getDefinitionId());
                    processInstance.setStatus(RunTimeStatus.EXCEPTION);
                    processInstance.setMsg("has no start node");
                    update(processInstance);
                    return;
                }
            }
        } else {
            currentNodeInstance = definition.nextNodeInstance(currentNodeInstance.getNodeInstanceId(), processInstance.getProcessParams());
        }
        if (currentNodeInstance == null) {
            log.error("processId {}: can't find next node instance ", processInstance.getId());
            processInstance.setStatus(RunTimeStatus.EXCEPTION);
            processInstance.setMsg("can't find next node instance ");
            update(processInstance);
            return;
        }
        currentNodeInstance.setStatus(RunTimeStatus.RUNNING);
        processInstance.setStatus(RunTimeStatus.RUNNING);
        processInstance.setCurrentNodeInstance(currentNodeInstance);
        update(processInstance);
        nodeInstanceService.execute(currentNodeInstance, processInstance);
    }


    private ProcessInstanceDO convertToDO(ProcessInstance processInstance) {
        if (processInstance == null) {
            return null;
        }
        ProcessInstanceDO processInstanceDO = new ProcessInstanceDO();
        BeanUtils.copyProperties(processInstance, processInstanceDO);

        if (processInstance.getDefinition() != null) {
            processInstanceDO.setDefinition(JSONObject.toJSONString(processInstance.getDefinition()));
        }
        if (processInstance.getApplyRequest() != null) {
            processInstanceDO.setApplyRequest(JSONObject.toJSONString(processInstance.getApplyRequest()));
        }
        if (processInstance.getProcessParams() != null) {
            processInstanceDO.setProcessParams(JSONObject.toJSONString(processInstance.getProcessParams()));
        }
        NodeInstance currentNodeInstance = processInstance.getCurrentNodeInstance();
        if (currentNodeInstance != null) {
            processInstanceDO.setCurrentNodeName(currentNodeInstance.getName());
            processInstanceDO.setCurrentNodeStatus(currentNodeInstance.getStatus().name());
        }
        LocalDateTime completedTime = processInstance.getCompletedTime();
        if (completedTime != null) {
            processInstanceDO.setCompletedTime(Timestamp.valueOf(completedTime));
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
        processInstance.setGmtCreated(processInstanceDO.getGmtCreated().toLocalDateTime());
        processInstance.setGmtModified(processInstanceDO.getGmtModified().toLocalDateTime());
        Timestamp completedTime = processInstanceDO.getCompletedTime();
        if (completedTime != null) {
            processInstance.setCompletedTime(completedTime.toLocalDateTime());
        }

        if (processInstanceDO.getDefinition() != null) {
            SimpleBPMDefinition simpleBPMDefinition = JSONObject.parseObject(processInstanceDO.getDefinition(), SimpleBPMDefinition.class);
            processInstance.setDefinition(simpleBPMDefinition);
            if (processInstanceDO.getCurrentNodeInstanceId() != null) {
                NodeInstance nodeInstance = simpleBPMDefinition.getNodeInstanceById(processInstanceDO.getCurrentNodeInstanceId());
                nodeInstance.setStatus(RunTimeStatus.valueOf(processInstanceDO.getCurrentNodeStatus()));
                processInstance.setCurrentNodeInstance(nodeInstance);
            }
        }
        if (processInstanceDO.getApplyRequest() != null) {
            processInstance.setApplyRequest(JSONObject.parseObject(processInstanceDO.getApplyRequest(), ApplyRequest.class));
        }
        if (processInstanceDO.getProcessParams() != null) {
            processInstance.setProcessParams(JSONObject.parseObject(processInstanceDO.getProcessParams(), new TypeReference<Map<String, Object>>() {
            }));
        }


        return processInstance;
    }
}
