package com.ps.judge.web.auth;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<AuthAuthorityBO> getAuthoritieBOsByRoleNames(String[] split);
    Set<GrantedAuthority> getAuthoritiesByRoleNames(String[] split);

    void deleteRule(int id);

    void addRule(AuthRoleBO authRoleBO);

    void modifyRule(AuthRoleBO authRoleBO);

    List<AuthRoleBO> queryAll();
}
