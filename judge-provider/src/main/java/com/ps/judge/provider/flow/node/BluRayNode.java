package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.exceptions.ProcessException;
import com.ps.judge.provider.flow.node.runtime.NodeResult;

import java.util.Map;

public class BluRayNode extends AsyncNode {

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public NodeResult callback(ProcessInstance flowInstance, Map<String, Object> params) {
        NodeResult nodeResult = new NodeResult();
        nodeResult.setSuccess(true);
        return nodeResult;
    }

    @Override
    public String getName() {
        return "bluRay";
    }
}
