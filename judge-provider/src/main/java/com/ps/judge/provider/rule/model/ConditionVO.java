package com.ps.judge.provider.rule.model;

import lombok.Data;

@Data
public class ConditionVO {
    private String operator;
    private String operand;
    private String variableCode;
    private Integer variableType;
    private Integer relation;
}
