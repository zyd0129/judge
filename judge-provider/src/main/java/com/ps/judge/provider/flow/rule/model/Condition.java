package com.ps.judge.provider.flow.rule.model;

import lombok.Data;

@Data
public class Condition {
    private String variableCode;
    private Integer variableType;
    private String operator;
    private String operand;
    private Integer relation;
}