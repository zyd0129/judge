package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigFlowRulePackageDO {
    private Integer id;
    private String flowCode;
    private Integer rulePackageVersionId;
    private Integer sort;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
