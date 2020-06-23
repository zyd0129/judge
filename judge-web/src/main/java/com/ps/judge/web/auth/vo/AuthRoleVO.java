package com.ps.judge.web.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthRoleVO {
    private int id;
    private String name;
    /**
     * json字符串，三级菜单权限
     */
    private List<FirstMenu> authorities;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
