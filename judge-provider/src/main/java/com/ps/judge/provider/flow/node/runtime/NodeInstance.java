package com.ps.judge.provider.flow.node.runtime;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.RunTimeStatus;
import com.ps.judge.provider.flow.node.AsyncNode;
import com.ps.judge.provider.flow.node.EndNode;
import com.ps.judge.provider.flow.node.Node;
import com.ps.judge.provider.flow.service.ProcessInstanceService;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 节点可以复用，即一个节点可以在多个process里定义，一个process也可以定义多个相同节点
 */
@Data
public class NodeInstance {

    private Node node;
    private String name;
    private RunTimeStatus status;
    private String msg;
    private String nodeInstanceId;

    private ProcessInstanceService flowInstanceService;

    public NodeInstance(ProcessInstanceService flowService) {
        this.flowInstanceService = flowService;
    }


    public void execute(ProcessInstance flowInstance) {
        NodeResult nodeResult = node.execute(flowInstance);
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
        flowInstance.execute();
    }

    public void callback(ProcessInstance flowInstance, Map<String, Object> params) {
        AsyncNode asyncNode = (AsyncNode) node;
        NodeResult nodeResult = asyncNode.callback(flowInstance, params);
        if (!nodeResult.isSuccess()) {
            msg = nodeResult.getMsg();
            status = RunTimeStatus.EXCEPTION;
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstanceService.update(flowInstance);
            return;
        }
        flowInstance.execute();
    }

    public String getName() {
        if (StringUtils.isNotEmpty(name)) {
            return node.getName() + "-" + nodeInstanceId;
        }
        return name;
    }
}
