package com.ts.judge.provider.flow.rule.context;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drools规则加载接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
@Slf4j
public class DroolsRuleContext implements RuleContext {
    private final Map<String, InternalKnowledgeBase> ruleContext = new ConcurrentHashMap<>();

    @Override
    public KieSession getKieSession(String flowCode) {
        if (this.existed(flowCode)) {
            InternalKnowledgeBase internalKnowledgeBase = this.ruleContext.get(flowCode);
            return internalKnowledgeBase.newKieSession();
        }
        return null;
    }

    @Override
    public StatelessKieSession getStatelessKieSession(String flowCode) {
        if (this.existed(flowCode)) {
            InternalKnowledgeBase internalKnowledgeBase = this.ruleContext.get(flowCode);
            return internalKnowledgeBase.newStatelessKieSession();
        }
        return null;
    }

    @Override
    public boolean add(String flowCode, String ruleStr) {
        InternalKnowledgeBase internalKnowledgeBase = this.load(ruleStr);
        if (Objects.isNull(internalKnowledgeBase)) {
            return false;
        }
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
            log.error("规则文件错误 ：{}", knowledgeBuilder.getErrors().toString());
            return null;
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
            log.error("规则文件错误 ：{}", knowledgeBuilder.getErrors().toString());
            return null;
        }
        InternalKnowledgeBase internalKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());
        return internalKnowledgeBase;
    }
}
