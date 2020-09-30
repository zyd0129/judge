package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import org.springframework.stereotype.Service;

@Service("txScoreNode")
public class TxScoreNode extends Node {

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "txScore";
    }
}
