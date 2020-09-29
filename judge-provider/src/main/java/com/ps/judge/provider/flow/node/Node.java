package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.exceptions.ProcessException;
import com.ps.judge.provider.flow.node.runtime.NodeResult;
import lombok.Data;


@Data
public abstract class Node {

    public abstract void process(ProcessInstance flowInstance) throws ProcessException;

    public NodeResult execute(ProcessInstance flowInstance) {
        NodeResult nodeResult = new NodeResult();
        try {
            process(flowInstance);
            nodeResult.setSuccess(true);
        } catch (ProcessException e) {
            nodeResult.setSuccess(false);
            nodeResult.setMsg(e.getMessage());
        }

        return nodeResult;
    }

    public abstract boolean isSync();

    public abstract String getName();
}
