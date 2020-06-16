package com.ps.judge.web.models;

import com.ps.common.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigFlowBO {

    private Integer id;
    private String flowCode;
    private String flowName;
    private String flowVersion;
    private String tenantName;
    private String tenantCode;
    private String productCode;
    private String productName;
    private Integer packageId;
    private String packageName;
    private String packageVersion;
    private String packageUrl;
    private String remark;

    private String operator;
    private Status status = Status.STOPPED;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
