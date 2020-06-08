package com.ps.judge.web.auth.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ps.judge.web.auth.objects.AuthTenantBO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
