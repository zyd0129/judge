package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiskParamDO {
    private Integer id;
    private String tenantId;
    private String applyId;
    private String inputParam;
    private String outputParam;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
