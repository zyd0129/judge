package com.ps.judge.provider.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigProductBO {
    private Integer id;
    private String tenantCode;
    private String productName;
    private String productCode;
    private Integer status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
