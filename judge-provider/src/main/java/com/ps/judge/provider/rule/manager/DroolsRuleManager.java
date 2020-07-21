package com.ps.judge.provider.rule.manager;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drools规则加载接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public class DroolsRuleManager implements RuleManager {
    private final Map<String, InternalKnowledgeBase> ruleContext = new ConcurrentHashMap<>();

    @Override
    public KieSession getKieSession(String flowCode) {
        InternalKnowledgeBase internalKnowledgeBase = this.ruleContext.get(flowCode);
        return internalKnowledgeBase.newKieSession();
    }

    @Override
    public boolean add(String flowCode, String ruleStr) {
        InternalKnowledgeBase internalKnowledgeBase = this.load(ruleStr);
        this.ruleContext.put(flowCode, internalKnowledgeBase);
        return true;
    }

    @Override
    public boolean add(String flowCode, List<String> ruleStrList) {
        InternalKnowledgeBase internalKnowledgeBase = this.load(ruleStrList);
        this.ruleContext.put(flowCode, internalKnowledgeBase);
        return true;
    }

    @Override
    public boolean remove(String flowCode) {
        this.ruleContext.remove(flowCode);
        return true;
    }

    @Override
    public boolean existed(String flowCode) {
        return this.ruleContext.containsKey(flowCode);
    }

    private InternalKnowledgeBase load(String ruleStr) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newByteArrayResource(ruleStr.getBytes(StandardCharsets.UTF_8)), ResourceType.RDRL);
        // Check the builder for errors
        if (knowledgeBuilder.hasErrors()) {
            //System.err.println("规则错误 ：" + knowledgeBuilder.getErrors().toString());
        }
        InternalKnowledgeBase internalKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());
        return internalKnowledgeBase;
    }

    private InternalKnowledgeBase load(List<String> ruleStrList) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        for (String ruleStr : ruleStrList) {
            knowledgeBuilder.add(ResourceFactory.newByteArrayResource(ruleStr.getBytes(StandardCharsets.UTF_8)), ResourceType.RDRL);
        }
        // Check the builder for errors
        if (knowledgeBuilder.hasErrors()) {
            //System.err.println("规则错误 ：" + knowledgeBuilder.getErrors().toString());
        }
        InternalKnowledgeBase internalKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());
        return internalKnowledgeBase;
    }
}
