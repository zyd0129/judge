package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTaskDO {
    private Integer id;
    private String tenantCode;
    private String productCode;
    private String flowCode;
    private String applyId;
    private String userId;
    private String userName;
    private String mobile;
    private String idCard;
    private String orderId;
    private String ip;
    private String deviceFingerPrint;
    private LocalDateTime transactionTime;
    private Integer taskStatus;
    private Integer auditScore;
    private String auditCode;
    private String callbackUrl;
    private Integer callbackCount;
    private LocalDateTime completeTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
