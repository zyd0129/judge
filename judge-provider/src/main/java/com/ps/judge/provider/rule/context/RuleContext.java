package com.ps.judge.provider.rule.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuleContext<T> {
    private final Map<String, T> ruleContext = new ConcurrentHashMap<>();

    public void put(String flowCode, T t) {
        this.ruleContext.put(flowCode, t);
    }

    public T get(String flowCode) {
        return this.ruleContext.get(flowCode);
    }

}
