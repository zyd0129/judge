package com.ts.judge.provider.flow.rule.builder;

import com.ts.judge.provider.flow.rule.model.Condition;
import com.ts.judge.provider.flow.rule.model.Rule;

import java.util.List;

/**
 * 规则生成模板
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public abstract class RuleTemplate {
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public String build(Rule rule) {
        StringBuilder ruleStr = new StringBuilder();
        ruleStr.append(this.buildImports());
        ruleStr.append(this.buildRule(rule));
        return new String(ruleStr);
    }

    public String build(List<Rule> ruleList) {
        StringBuilder ruleStr = new StringBuilder();
        ruleStr.append(this.buildImports());
        for (Rule rule : ruleList) {
            ruleStr.append(this.buildRule(rule));
        }
        return new String(ruleStr);
    }

    private String buildRule(Rule rule) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.buildAttributes(rule));
        sb.append(this.buildLHS(rule.getConditionList()));
        sb.append(this.buildRHS(rule));
        return new String(sb);
    }

    abstract String buildImports();

    abstract String buildAttributes(Rule rule);

    abstract String buildLHS(List<Condition> conditionList);

    abstract String buildRHS(Rule rule);
}
