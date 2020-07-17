package com.ps.judge.provider.rule.load;

import org.apache.poi.ss.formula.functions.T;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.UnsupportedEncodingException;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public class DroolsLoanImpl implements Loan {

    private static final String CHARSET_NAME = "UTF-8";

    @Override
    public InternalKnowledgeBase load(String ruleStr) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        try {
            knowledgeBuilder.add(ResourceFactory.newByteArrayResource(ruleStr.getBytes(CHARSET_NAME)), ResourceType.RDRL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Check the builder for errors
        if (knowledgeBuilder.hasErrors() ) {
            System.err.println("规则错误 ：" + knowledgeBuilder.getErrors().toString());
        }

        InternalKnowledgeBase internalKnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        internalKnowledgeBase.addPackages(knowledgeBuilder.getKnowledgePackages());

        return internalKnowledgeBase;
    }
}
