package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRulePackageVersionDO {
    private Integer id;
    private Integer rulePackageId;
    private Integer version;
    private Integer operatorId;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
