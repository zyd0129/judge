package com.ps.judge.provider.rule.model;

import lombok.Data;

import java.util.List;

@Data
public class RuleVO {
    private String ruleCode;
    private String ruleName;
    private String ruleVersion;
    private String ruleFlowGroup;
    private String agendaGroup;
    private Integer salience;
    private Integer score;
    private String result;
    List<ConditionVO> conditionList;
}
