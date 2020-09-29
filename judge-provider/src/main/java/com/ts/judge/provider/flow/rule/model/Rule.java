package com.ts.judge.provider.flow.rule.model;

import lombok.Data;

import java.util.List;

@Data
public class Rule {
    private String ruleCode;
    private String ruleName;
    private String ruleVersion;
    private String ruleFlowGroup;
    private String agendaGroup;
    private String rulePackageName;
    private String rulePackageVersion;
    private Integer salience;
    private Integer score;
    private String result;
    List<Condition> conditionList;
}
