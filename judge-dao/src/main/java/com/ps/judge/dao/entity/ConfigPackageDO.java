package com.ps.judge.dao.entity;

import java.time.LocalDateTime;

public class ConfigPackageDO {
    private Integer id;
    private String tenantCode;
    private Integer taskId;
    private String applyId;
    private String inputRawParam;
    private String outputRawParam;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
