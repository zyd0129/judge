package com.ps.judge.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthDepartmentDO {
    private Integer id;
    private String name;
    //person in charge
    private Integer managerId;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    private AuthUserDO manager;
    private List<AuthUserDO> members;
}
