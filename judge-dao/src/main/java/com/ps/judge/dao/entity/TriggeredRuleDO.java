package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TriggeredRuleDO {
    private Integer id;
    private String tenantId;
    private String applyId;
    private String flow;
    private String rulePackageCode;
    private String ruleCode;
    private String param;
    private LocalDateTime createTime;
}
