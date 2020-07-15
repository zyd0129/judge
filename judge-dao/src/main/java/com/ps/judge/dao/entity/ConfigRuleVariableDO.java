package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableDO {
    private Integer id;
    private String tenantCode;
    private Integer levelId;
    private String levelCode;
    private Integer groupId;
    private String groupCode;
    private String code;
    private String name;
    private Integer type;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
