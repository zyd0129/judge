package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.flow.node.NodeInstance;
import org.springframework.stereotype.Service;

@Service("endNode")
public class EndNode extends Node {

    @Override
    public void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "end";
    }
}
