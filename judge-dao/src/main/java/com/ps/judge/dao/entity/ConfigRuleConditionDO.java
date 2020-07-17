package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleConditionDO {
    private Integer id;
    private Integer ruleId;
    private String name;
    private String operator;
    private String operand;
    private String function;
    private String variableCode;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
