package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskParamDO {
    private Integer id;
    private String tenantCode;
    private Integer taskId;
    private String applyId;
    private String inputRawParam;
    private String outputRawParam;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
