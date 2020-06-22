package com.ps.judge.api.entity;

import lombok.Data;

import java.util.List;

@Data
public class NodeResultVO {
    private Integer index;
    private Integer auditScore;
    private String auditCode;
    private String rulePackageCode;
    private List<TriggeredRuleVO> triggeredRules;
}
