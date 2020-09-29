package com.ts.judge.provider;

import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.UnsupportedEncodingException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class JudgeProviderApplicationTests {
    @Test
    public void contextLoads() {
        System.out.println(System.currentTimeMillis());
    }

    private void verifyRule(String ruleString) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        try {
            knowledgeBuilder.add(ResourceFactory.newByteArrayResource(ruleString.getBytes("UTF-8")), ResourceType.RDRL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (knowledgeBuilder.hasErrors() ) {
            System.err.println("规则验证错误:" + knowledgeBuilder.getErrors().toString());
        } else {
            System.err.println("规则验证正确!" );
        }
    }
}
