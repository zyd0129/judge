package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableDO {
    private Integer id;
    private String name;
    private String code;
    private String level;
    private String group;
    private String type;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
