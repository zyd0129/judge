package com.ps.judge.web.pojo.bo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleConditionBO {
    private Integer id;
    private Integer ruleId;
    private String name;
    private String operator;
    private String operand;
    private String function;
    private String variableCode;
    private Integer variableType;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
