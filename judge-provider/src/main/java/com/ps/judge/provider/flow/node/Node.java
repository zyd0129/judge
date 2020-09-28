package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.FlowInstance;
import lombok.Data;

import java.util.Map;

@Data
public abstract class Node {

    public abstract void  process(Map<String, Object> flowParams);

    public NodeResult execute(Map<String, Object> flowParams, FlowInstance flowInstance){
        process(flowParams);

        NodeResult nodeResult = new NodeResult();
        nodeResult.setSuccess(true);
        return nodeResult;
    }

    public abstract boolean isSync();

    public abstract String getName();
}
