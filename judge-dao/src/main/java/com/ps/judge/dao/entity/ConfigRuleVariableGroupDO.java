package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableGroupDO {
    private Integer id;
    private String tenantCode;
    private Integer levelId;
    private String levelCode;
    private String code;
    private String name;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
