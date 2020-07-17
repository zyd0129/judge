package com.ps.judge.provider.rule.builder;

import com.ps.judge.provider.rule.model.ConditionVO;
import com.ps.judge.provider.rule.model.RuleVO;

import java.util.List;

/**
 * 规则生成模板
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public abstract class RuleTemplate {
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public String build(RuleVO rule) {
        StringBuilder ruleStr = new StringBuilder();
        ruleStr.append(this.buildImports());
        ruleStr.append(this.buildRule(rule));
        return new String(ruleStr);
    }

    public String build(List<RuleVO> ruleList) {
        StringBuilder ruleStr = new StringBuilder();
        ruleStr.append(this.buildImports());
        for (RuleVO rule : ruleList) {
            ruleStr.append(this.buildRule(rule));
        }
        return new String(ruleStr);
    }

    private String buildRule(RuleVO rule){
        StringBuilder sb = new StringBuilder();
        sb.append(this.buildAttributes(rule));
        sb.append(this.buildLHS(rule.getConditionList()));
        sb.append(this.buildRHS(rule));
        return new String(sb);
    }

    abstract String buildImports();

    abstract String buildAttributes(RuleVO rule);

    abstract String buildLHS(List<ConditionVO> conditionList);

    abstract String buildRHS(RuleVO rule);
}
