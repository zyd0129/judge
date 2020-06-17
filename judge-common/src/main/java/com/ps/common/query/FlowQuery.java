package com.ps.common.query;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class FlowQuery {
    private String tenantCode;
    private String productCode;
    private String packageName;
    private String flowName;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private String status;
    private boolean fuzzy;
}
