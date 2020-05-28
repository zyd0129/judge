package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskTriggeredRuleDO {
    private Integer id;
    private String tenantCode;
    private Integer taskId;
    private String applyId;
    private String auditFlow;
    private String rulePackageCode;
    private String rulePackageName;
    private String rulePackageVersion;
    private String ruleCode;
    private String ruleName;
    private String ruleVersion;
    private String expression;
    private String param;
    private LocalDateTime createTime;
}
