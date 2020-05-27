package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigProductDO {
    private Integer id;
    private String tenantCode;
    private String productName;
    private String productCode;
    private Integer status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
