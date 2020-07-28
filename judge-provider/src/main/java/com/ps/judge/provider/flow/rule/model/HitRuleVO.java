package com.ps.judge.provider.flow.rule.model;

import lombok.Data;

@Data
public class HitRuleVO {
    private String ruleCode;
    private String ruleName;
    private String ruleVersion;
    private String rulePackageCode;
    private String rulePackageName;
    private String rulePackageVersion;
    private String expression;
    private String condition;
    private String param;
    private Integer score;
    private String result;
}
