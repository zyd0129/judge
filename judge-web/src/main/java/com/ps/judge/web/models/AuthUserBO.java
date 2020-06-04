package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUserBO {
    private Integer id;
    /**
     * 登录名
     */
    private String username;
    /**
     * 用户名称
     */
    private String name;
    private String password;
    private String roles;
    private String authorities;
    private boolean expired;
    private boolean locked;
    private boolean credentialsExpired;
    private boolean enabled;

    private String mobile;
    private String tenants;
    private String department;
    private String operator;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

}
