package com.ps.judge.web.auth.req;

import com.ps.judge.web.auth.objects.AuthTenantBO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthUserModifyReq {
    private Integer id;
    /**
     * 登录名
     */
    private String username;

    /**
     * 用户名称
     */
    private String name;
    private String roles;


    private String mobile;
    private List<AuthTenantBO> tenants;
    private String department;

    private String operator;


    private LocalDateTime gmtModified;
}
