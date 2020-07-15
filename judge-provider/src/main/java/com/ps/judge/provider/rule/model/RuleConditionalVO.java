package com.ps.judge.provider.rule.model;

import lombok.Data;

@Data
public class RuleConditionalVO {
    private String operator;
    private String operand;
    private String function;
    private RuleVariableVO ruleVariable;
}
