package com.ps.judge.web.auth.objects;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class AuthUserBO  implements UserDetails {
    private Integer id;
    /**
     * 登录名
     */
    private String username;
    /**
     * 用户名称
     */
    private String name;
    private String password;
    private String roles;
    private Set<GrantedAuthority> authorities;

    private boolean expired;
    private boolean locked;
    private boolean credentialsExpired;
    private boolean enabled;

    private String mobile;
    private String tenants;
    private String department;
    private String operator;


    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
