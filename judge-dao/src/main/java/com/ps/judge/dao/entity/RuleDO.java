package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RuleDO {
    private Integer id;
    private String tenantId;
    private String packageCode;
    private String code;
    private String name;
    private String param;
    private String expression;
    private String description;
    private String result;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
