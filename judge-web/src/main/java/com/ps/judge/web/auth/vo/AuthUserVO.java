package com.ps.judge.web.auth.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ps.judge.web.auth.objects.AuthTenantBO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class AuthUserVO {
    private Integer id;
    /**
     * 登录名
     */
    private String username;
    @JsonIgnore
    private String password;

    private String rawPassword;
    /**
     * 用户名称
     */
    private String name;
    private String roles;
    private Set<GrantedAuthority> authorities;


    private String mobile;
    private List<AuthTenantBO> tenants;
    private String department;
    private String operator;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
