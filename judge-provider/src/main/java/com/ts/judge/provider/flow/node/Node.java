package com.ts.judge.provider.flow.node;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public abstract class Node {

    public abstract void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException;

    public NodeResult execute(ProcessInstance flowInstance, NodeInstance nodeInstance) {
        NodeResult nodeResult = new NodeResult();
        try {
            process(flowInstance, nodeInstance);
            nodeResult.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            nodeResult.setSuccess(false);
            nodeResult.setMsg(e.getMessage());
        }

        return nodeResult;
    }

    public abstract boolean isSync();

    public abstract String getName();
}
