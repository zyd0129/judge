package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthDepartmentDO {
    private Integer id;
    private String name;
    //person in charge
    private String pic;
    private String members;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
