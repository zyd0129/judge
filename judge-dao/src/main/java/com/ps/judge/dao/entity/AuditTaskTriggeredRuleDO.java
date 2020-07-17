package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskTriggeredRuleDO {
    private Integer id;
    private String tenantCode;
    private Integer taskId;
    private String applyId;
    private String flowCode;
    private Integer index;
    private String rulePackageCode;
    private String rulePackageName;
    private String rulePackageVersion;
    private String ruleCode;
    private String ruleName;
    private String ruleVersion;
    private String expression;
    private String condition;
    private String param;
    private String result;
    private Integer score;
    private LocalDateTime gmtCreate;

}
