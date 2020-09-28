package com.ps.judge.provider.flow.node;

import java.util.Map;

public class TxScoreNode extends Node{
    @Override
    public void process(Map<String, Object> flowParams) {

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
