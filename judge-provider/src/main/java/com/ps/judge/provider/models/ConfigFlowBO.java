package com.ps.judge.provider.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigFlowBO {
    private Integer id;
    private String flowCode;
    private String flowName;
    private String tenantCode;
    private String productCode;
    private Integer curPackageId;
    private String curPackageGroup;
    private String curPackageArtifact;
    private String curPackageVersion;
    private Integer status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
