package com.ps.judge.provider.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ps.judge.provider.flow.bpmn.SimpleBPMDefinition;
import com.ps.judge.provider.flow.node.runtime.NodeInstance;
import com.ps.judge.provider.flow.service.ProcessInstanceService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Slf4j
public class ProcessInstance {

    @JsonIgnore
    private ProcessInstanceService processInstanceService;

    private Integer id;
    private Integer definitionId;
    private SimpleBPMDefinition definition;
    private NodeInstance currentNodeInstance;
    private String msg;

    /**
     * 流程变量
     */
    private Map<String, Object> processParams;
    /**
     * 流程入参
     */
    private Map<String, Object> applyRequest;

    private RunTimeStatus status;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtCompleted;

    public ProcessInstance(ProcessInstanceService processInstanceService) {
        this.processInstanceService = processInstanceService;
    }

    public void execute() {
        /**
         *  更新 flowInstance
         *  1. 设置next
         *  2.更新节点状态
         *  3.更新状态时间
         */
        if (status.equals(RunTimeStatus.CREATED)) {
            currentNodeInstance = definition.getStartNodeInstance();
            if (currentNodeInstance == null) {
                log.error("process definition {} has no start node", definitionId);
                status = RunTimeStatus.EXCEPTION;
                msg = "has no start node";
                gmtModified = LocalDateTime.now();
                processInstanceService.update(this);
                return;
            }
        } else {
            currentNodeInstance = definition.nextNodeInstance(currentNodeInstance.getNodeInstanceId(), processParams);
        }
        currentNodeInstance.setStatus(RunTimeStatus.RUNNING);
        status = RunTimeStatus.RUNNING;
        gmtModified = LocalDateTime.now();
        processInstanceService.update(this);

        currentNodeInstance.execute(this);
    }
}
