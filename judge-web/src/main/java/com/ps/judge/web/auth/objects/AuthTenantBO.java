package com.ps.judge.web.auth.objects;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthTenantBO {
    private Integer id;
    private String tenantName;
    private String tenantCode;
    private String secretKey;
    /**
     * 0 禁用
     * 1 启用
     */
    private Boolean status;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
