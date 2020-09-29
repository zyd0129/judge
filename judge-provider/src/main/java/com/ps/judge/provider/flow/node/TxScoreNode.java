package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.exceptions.ProcessException;

import java.util.Map;

public class TxScoreNode extends Node{

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public String getName() {
        return "txScore";
    }
}
