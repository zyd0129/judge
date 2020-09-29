package com.ts.judge.provider.flow.node;

import com.ts.judge.provider.flow.ProcessInstance;

import java.util.Map;

public abstract class AsyncNode extends Node {
    @Override
    public boolean isSync() {
        return false;
    }

    public abstract NodeResult callback(ProcessInstance flowInstance, Map<String, Object> params);
}
