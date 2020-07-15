package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleConditionalDO {
    private Integer id;
    private String ruleCode;
    private Integer ruleVersion;
    private String expression;
    private String conditionValue;
    private String function;
    private Integer variableId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
