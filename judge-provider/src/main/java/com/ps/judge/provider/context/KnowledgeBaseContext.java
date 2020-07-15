package com.ps.judge.provider.context;

import org.drools.core.impl.InternalKnowledgeBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KnowledgeBaseContext {
    private final Map<String, InternalKnowledgeBase> internalKnowledgeBaseMap = new ConcurrentHashMap<>();

    public void putInternalKnowledgeBase(String flowCode, InternalKnowledgeBase internalKnowledgeBase) {
        this.internalKnowledgeBaseMap.putIfAbsent(flowCode, internalKnowledgeBase);
    }

    public InternalKnowledgeBase getInternalKnowledgeBase(String flowCode) {
        return this.internalKnowledgeBaseMap.get(flowCode);
    }


}
