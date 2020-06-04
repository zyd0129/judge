package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthRoleBO {
    private int id;
    private String name;
    private String authorities;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
