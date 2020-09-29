package com.ps.judge.provider.flow.node;

import com.ps.judge.provider.flow.ProcessInstance;
import com.ps.judge.provider.flow.node.runtime.NodeResult;

import java.util.Map;

public abstract class AsyncNode extends Node {
    @Override
    public boolean isSync() {
        return false;
    }

    public abstract NodeResult callback(ProcessInstance flowInstance, Map<String, Object> params);
}
