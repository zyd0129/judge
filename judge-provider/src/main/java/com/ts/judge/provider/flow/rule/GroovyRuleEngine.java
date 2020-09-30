package com.ts.judge.provider.flow.rule;

import com.ts.judge.provider.flow.rule.executor.ExpressionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GroovyRuleEngine implements RuleEngine {

    @Autowired
    ExpressionExecutor expressionExecutor;

    @Override
    public RulePackageResult execute(RulePackage rulePackage, Map<String, Object> processParams) {
        RulePackageResult rulePackageResult = new RulePackageResult();
        List<Rule> ruleList = rulePackage.getRuleList();
        for (Rule rule : ruleList) {
            boolean result = expressionExecutor.executor(rule.getExpression(), processParams);
            if (result) {
                rulePackageResult.addRule(rule);
            }
        }
        rulePackageResult.result();
        return rulePackageResult;
    }
}
