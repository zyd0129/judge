package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.exceptions.ProcessException;

import java.util.Map;

public class RuleNode extends Node {
    private String packageId;

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {

    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "rule";
    }
}
