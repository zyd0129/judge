package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigFlowDO {
    private Integer id;
    private String flowCode;
    private String flowName;
    private String tenantCode;
    private String productCode;
    private Integer packageId;
    private String packageUrl;
    private String remark;

    private String operator;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
