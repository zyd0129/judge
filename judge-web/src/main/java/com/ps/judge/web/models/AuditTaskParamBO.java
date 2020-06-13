package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskParamBO {
    private Integer id;
    private String tenantCode;
    private Integer taskId;
    private String applyId;
    private String inputRawParam;
    private String outputRawParam;
    private String varResult;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
