package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthTenantDO {
    private Integer id;
    private String name;
    private String code;
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