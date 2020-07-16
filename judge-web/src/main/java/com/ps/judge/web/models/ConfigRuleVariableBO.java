package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableBO {
    private Integer id;
    private Integer levelId;
    private Integer groupId;
    private String code;
    private String name;
    private Integer type;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
