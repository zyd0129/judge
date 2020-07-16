package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleDO {
    private Integer id;
    private String tenantCode;
    private Integer rulePackageId;
    private String code;
    private String name;
    private Integer version;
    private Integer salience;
    private Integer status;
    private Integer score;
    private String result;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
