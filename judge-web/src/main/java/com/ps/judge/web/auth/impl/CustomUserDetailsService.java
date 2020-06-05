package com.ps.judge.web.auth.impl;

import com.ps.judge.web.auth.RoleService;
import com.ps.judge.web.auth.UserService;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserBO authUserBO = userService.getByUsername(username);
        if (authUserBO == null) {
            throw new UsernameNotFoundException("username not found");
        }
        String roles = authUserBO.getRoles();
        Set<GrantedAuthority> authoritySet = null;
        if (!StringUtils.isEmpty(roles)) {

            authoritySet = roleService.getAuthoritiesByRoleNames(roles.split(","));
        }
        authUserBO.setAuthorities(authoritySet);
        return authUserBO;
    }
}
