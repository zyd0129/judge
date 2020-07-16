package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableGroupDO {
    private Integer id;
    private Integer levelId;
    private String code;
    private String name;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
