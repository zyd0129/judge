package com.ps.judge.web.auth.objects;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AuthDepartmentBO {
    private Integer id;
    private String name;
    //person in charge
    private String pic;
    private Set<AuthUserBO> members;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
