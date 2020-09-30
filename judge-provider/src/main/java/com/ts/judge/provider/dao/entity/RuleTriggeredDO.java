package com.ts.judge.provider.dao.entity;

import lombok.Data;

@Data
public class RuleTriggeredDO {
    private Integer ruleId;
    private Integer processInstanceId;
}
