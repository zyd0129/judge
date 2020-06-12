package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigPackageDO {
    private Integer id;
    private String tenantCode;
    private String tenantName;
    private String productCode;
    private String productName;

    private String packageName;
    private String version;
    private String url;
    private String flow;
    private String remark;
    private Integer status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
