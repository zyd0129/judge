package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.AsyncNode;
import com.ts.judge.provider.flow.node.NodeResult;
import com.ts.judge.provider.third.bluRay.BluRayClient;
import com.ts.judge.provider.third.bluRay.BluRayParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("bluRayNode")
public class BluRayNode extends AsyncNode {
    @Autowired
    BluRayClient bluRayClient;

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {
        BluRayParams bluRayParams = new BluRayParams();
        bluRayClient.send(bluRayParams);
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
