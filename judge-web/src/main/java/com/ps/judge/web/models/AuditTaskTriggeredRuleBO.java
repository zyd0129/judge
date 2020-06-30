package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskTriggeredRuleBO {
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
    private LocalDateTime gmtCreate;

}
