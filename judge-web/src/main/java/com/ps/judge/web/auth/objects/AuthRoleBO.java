package com.ps.judge.web.auth.objects;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthRoleBO {
    private int id;
    private String name;
    /**
     * json字符串，三级菜单权限
     */
    private List<AuthAuthorityBO> authorities;

    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
