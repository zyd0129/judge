package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigProductDO {
    private Integer id;
    private String tenantCode;
    private String tenantName;
    private String productName;
    private String productCode;
    private Integer status;
    private String operator;
    private String remark;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
