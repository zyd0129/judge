package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import org.springframework.stereotype.Service;

@Service("juryNode")
public class JuryNode extends Node {

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public String getName() {
        return "jury";
    }
}
