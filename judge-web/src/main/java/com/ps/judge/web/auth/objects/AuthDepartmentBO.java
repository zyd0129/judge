package com.ps.judge.web.auth.objects;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthDepartmentBO {
    private Integer id;
    private String name;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    private AuthUserBO manager;
    private List<AuthUserBO> members;
}
