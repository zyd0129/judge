package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUserDO {
    private Integer id;
    private String username;
    private String name;
    private String password;
    private String roles;
    private boolean expired;
    private boolean locked;
    private boolean credentialsExpired;
    private boolean enabled;

    private Integer userType;

    private String mobile;
    private String tenants;
    private String department;
    private String operator;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

}
