package com.ps.judge.provider.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigPackageBO {
    private Integer id;
    private String group;
    private String artifact;
    private String version;

    private Integer flowId;
    private String tenantCode;
    private String productCode;

    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
