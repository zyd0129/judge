package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.FlowInstance;
import com.ps.judge.provider.flow.RunTimeStatus;
import com.ps.judge.provider.service.FlowInstanceService;
import lombok.Data;

import java.util.Map;

@Data
public class NodeInstance {
    private FlowInstanceService flowInstanceService;

    public NodeInstance(FlowInstanceService flowService) {
        this.flowInstanceService = flowService;
    }

    private Node node;
    private String name;
    private RunTimeStatus status;
    private String msg;

    public void execute(Map<String, Object> flowParams, FlowInstance flowInstance) {
        NodeResult nodeResult = node.execute(flowParams, flowInstance);
        if (!nodeResult.isSuccess()) {
            msg = nodeResult.getMsg();
            status = RunTimeStatus.EXCEPTION;
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstanceService.update(flowInstance);
            return;
        }

        if (!node.isSync()) {
            status = RunTimeStatus.WAITING;
            flowInstance.setStatus(RunTimeStatus.WAITING);
            flowInstanceService.update(flowInstance);
            return;
        }
        if (node instanceof EndNode) {
            status = RunTimeStatus.COMPLETED;
            flowInstance.setStatus(RunTimeStatus.COMPLETED);
            flowInstanceService.update(flowInstance);
            return;
        }
        flowInstance.execute(flowParams);
    }
}
