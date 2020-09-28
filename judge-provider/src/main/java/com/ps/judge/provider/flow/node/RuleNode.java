package com.ps.judge.provider.flow.node;

import java.util.Map;

public class RuleNode extends Node {
    private String packageId;


    @Override
    public void process(Map<String, Object> flowParams) {

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
