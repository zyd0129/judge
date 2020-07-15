package com.ps.judge.provider.rule.model;

import lombok.Data;

@Data
public class RuleConditionalVO {
    private String expression;
    private String conditionValue;
    private String function;
    private RuleVariableVO ruleVariable;
}
