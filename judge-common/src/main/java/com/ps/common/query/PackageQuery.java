package com.ps.common.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PackageQuery {
    private String tenantCode;
    private String productCode;
    private String packageName;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private LocalDateTime gmtModifiedFrom;
    private LocalDateTime gmtModifiedTo;
    private String status;
}
