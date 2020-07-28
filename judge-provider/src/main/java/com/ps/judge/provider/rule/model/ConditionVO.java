package com.ps.judge.provider.rule.model;

import lombok.Data;

@Data
public class ConditionVO {
    private String variableCode;
    private Integer variableType;
    private String operator;
    private String operand;
    private Integer relation;
}
