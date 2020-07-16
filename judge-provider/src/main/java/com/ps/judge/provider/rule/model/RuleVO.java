package com.ps.judge.provider.rule.model;

import lombok.Data;

import java.util.List;

@Data
public class RuleVO {
    private String rule;
    private String ruleFlowGroup;
    private String agendaGroup;
    private Integer salience;
    private Integer score;
    private String result;
    List<RuleConditionalVO> ruleConditionalList;
}
