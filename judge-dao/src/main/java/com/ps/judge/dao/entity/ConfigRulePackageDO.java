package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRulePackageDO {
    private Integer id;
    private String tenantCode;
    private String code;
    private String name;
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
