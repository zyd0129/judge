package com.ps.common.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductQuery {
    private String tenantName;
    private String productName;
    private String productCode;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private LocalDateTime gmtModifiedFrom;
    private LocalDateTime gmtModifiedTo;
    private String status;
}
