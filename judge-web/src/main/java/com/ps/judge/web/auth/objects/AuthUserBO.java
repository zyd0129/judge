package com.ps.judge.web.auth.objects;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class AuthUserBO implements UserDetails {
    private Integer id;
    /**
     * 登录名
     */
    private String username;
    /**
     * 用户名称
     */
    private String name;
    @JsonIgnore
    @JSONField(serialize = false)
    private String password;
    private String roles;
    private Set<SimpleGrantedAuthority> authorities;

    @JsonIgnore
    @JSONField(serialize = false)
    private boolean expired;
    @JsonIgnore
    @JSONField(serialize = false)
    private boolean locked;
    @JsonIgnore
    @JSONField(serialize = false)
    private boolean credentialsExpired;
    @JsonIgnore
    @JSONField(serialize = false)
    private boolean enabled;

    private Integer userType;

    private String mobile;
    private List<AuthTenantBO> tenants;
    private String department;
    private String operator;

    @JsonIgnore
    @JSONField(serialize = false)
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
