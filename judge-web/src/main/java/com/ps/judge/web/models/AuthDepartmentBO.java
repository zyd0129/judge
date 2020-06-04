package com.ps.judge.web.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthDepartmentBO {
    private Integer id;
    private String name;
    private String members;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
