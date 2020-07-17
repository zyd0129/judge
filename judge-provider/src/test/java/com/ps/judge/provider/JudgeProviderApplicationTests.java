package com.ps.judge.provider;

import com.ps.judge.provider.rule.builder.DroolsRuleTemplate;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.model.RuleVO;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class JudgeProviderApplicationTests {
    @Test
    public void contextLoads() {
        VariableVO var1  = new VariableVO();
        var1.setLevelCode("person");
        var1.setGroupCode("sex");
        var1.setVarCode("s");
        var1.setType(0);

        VariableVO var2  = new VariableVO();
        var2.setLevelCode("person");
        var2.setGroupCode("sex");
        var2.setVarCode("s");
        var2.setType(0);

        ConditionVO condition1 = new ConditionVO();
        condition1.setOperator(">");
        condition1.setOperand("1");
        condition1.setVariable(var1);

        ConditionVO condition2 = new ConditionVO();
        condition2.setOperator("<");
        condition2.setOperand("5");
        condition2.setVariable(var2);

        List<ConditionVO> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        conditionList.add(condition2);

        RuleVO rule1 = new RuleVO();
        rule1.setRuleCode("a0001");
        rule1.setRuleName("性别为男");
        rule1.setRuleVersion("100");
        rule1.setRuleFlowGroup("a1");
        rule1.setAgendaGroup("a1");
        rule1.setSalience(100);
        rule1.setScore(10);
        rule1.setResult("0");
        rule1.setConditionList(conditionList);

        RuleVO rule2 = new RuleVO();
        rule2.setRuleCode("a0002");
        rule2.setRuleName("性别为女");
        rule2.setRuleVersion("10");
        rule2.setRuleFlowGroup("a2");
        rule2.setAgendaGroup("a2");
        rule2.setSalience(99);
        rule2.setScore(1);
        rule2.setResult("1");
        rule2.setConditionList(conditionList);

        RuleTemplate ruleTemplate = new DroolsRuleTemplate();

        //String ruleString1 = ruleTemplate.build(rule1);
        //System.err.println(ruleString1);
        //this.verifyRule(ruleString1);

        List<RuleVO> ruleList = new ArrayList<>();
        ruleList.add(rule1);
        ruleList.add(rule2);
        String ruleString2 = ruleTemplate.build(ruleList);
        System.err.println(ruleString2);
        this.verifyRule(ruleString2);
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
