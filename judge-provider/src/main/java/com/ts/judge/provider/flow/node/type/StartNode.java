package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.flow.node.NodeInstance;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("startNode")
public class StartNode extends Node {

    @Override
    protected Map<String, Object> process(Map<String, Object> inputParams, NodeInstance nodeInstance) {
        return null;
    }


    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "start";
    }
}
