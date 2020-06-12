package com.ps.judge.web.models;

import com.ps.common.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigPackageBO {
    private Integer id;
    private String tenantCode;
    private String tenantName;
    private String productCode;
    private String productName;

    private String remark;
    private String packageName;
    private String version;
    private String url;
    private String flow;
    private Status status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
