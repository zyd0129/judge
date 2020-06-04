package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthRoleDO {
    private int id;
    private String name;
    private String authorities;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
