package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiskDO {
    private Integer id;
    private String applyId;
    private String flow;
    private String tenantId;
    private String productId;
    private String userId;
    private String userName;
    private String mobile;
    private String idCard;
    private String orderId;
    private String ip;
    private String deviceFingerPrint;
    private LocalDateTime transactionTime;
    private Integer auditStatus;
    private Integer auditScore;
    private String auditCode;
    private String callbackUrl;
    private Integer callbackCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
