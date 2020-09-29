package com.ts.judge.provider.flow.node;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
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
