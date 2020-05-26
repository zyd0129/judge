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
    private ConfigPackageBO currentPackage;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
