package com.ps.judge.web.auth;

import com.ps.common.query.QueryParams;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Set<AuthAuthorityBO> getAuthorityBOsByRoleNames(String[] split);
    Set<GrantedAuthority> getAuthoritiesByRoleNames(String[] split);

    void deleteRule(int id);

    void addRule(AuthRoleBO authRoleBO);

    void modifyRule(AuthRoleBO authRoleBO);

    List<AuthRoleBO> queryAll();

    AuthRoleBO getById(int id);

    List<AuthRoleBO> query(QueryParams<AuthRoleBO> queryParams);
}
