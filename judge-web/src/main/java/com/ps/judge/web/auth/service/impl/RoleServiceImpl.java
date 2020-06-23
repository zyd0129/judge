package com.ps.judge.web.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.common.query.QueryParams;
import com.ps.common.query.RoleQuery;
import com.ps.judge.dao.entity.AuthRoleDO;
import com.ps.judge.dao.mapper.RoleMapper;
import com.ps.judge.web.auth.service.RoleService;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public Set<AuthAuthorityBO> getAuthorityBOsByRoleNames(String[] names) {
        if (names == null) {
            return null;
        }
        List<AuthRoleDO> authRoleDOList = roleMapper.queryByNames(names);
        if (authRoleDOList == null) {
            return null;
        }
        List<AuthAuthorityBO> authAuthorityBOList = new ArrayList<>();
        for (AuthRoleDO authRoleDO : authRoleDOList) {
            AuthRoleBO authAuthorityBO = convertToBO(authRoleDO);
            if (authAuthorityBO.getAuthorities() != null) {
                authAuthorityBOList.addAll(authAuthorityBO.getAuthorities());
            }
        }
        if (authAuthorityBOList.size() == 0) {
            return null;
        }

        return new HashSet<>(authAuthorityBOList);
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthoritiesByRoleNames(String[] split) {
        Set<AuthAuthorityBO> authAuthorityBOList = getAuthorityBOsByRoleNames(split);
        if (authAuthorityBOList == null) {
            return null;
        }
        return authAuthorityBOList.stream().map(authAuthorityBO -> new SimpleGrantedAuthority(authAuthorityBO.getName())).collect(Collectors.toSet());
    }

    @Override
    public void deleteRule(int id) {
        roleMapper.delete(id);
    }

    @Override
    public void addRule(AuthRoleBO authRoleBO) {
        AuthRoleDO authRoleDO = convertToDO(authRoleBO);
        roleMapper.insert(authRoleDO);
    }

    @Override
    public void modifyRule(AuthRoleBO authRoleBO) {
        AuthRoleDO authRoleDO = convertToDO(authRoleBO);
        roleMapper.update(authRoleDO);
    }

    @Override
    public List<AuthRoleBO> queryAll() {
        List<AuthRoleDO> authRoleDOList = roleMapper.listAll();
        return convertToBOs(authRoleDOList);
    }

    @Override
    public AuthRoleBO getById(int id) {
        return convertToBO(roleMapper.getById(id));
    }

    @Override
    public List<AuthRoleBO> query(QueryParams<RoleQuery> queryParams) {
        return convertToBOs(roleMapper.query(queryParams));
    }

    @Override
    public int total(QueryParams<RoleQuery> query) {
        return roleMapper.total(query);
    }

    private AuthRoleDO convertToDO(AuthRoleBO authRoleBO) {
        if (authRoleBO == null) {
            return null;
        }
        AuthRoleDO authRoleDO = new AuthRoleDO();
        BeanUtils.copyProperties(authRoleBO, authRoleDO);
        if (authRoleBO.getAuthorities() != null) {
            authRoleDO.setAuthorities(JSON.toJSONString(authRoleBO.getAuthorities()));
        }
        return authRoleDO;
    }

    private AuthRoleBO convertToBO(AuthRoleDO authRoleDO) {
        if (authRoleDO == null) {
            return null;
        }

        AuthRoleBO authRoleBO = new AuthRoleBO();
        BeanUtils.copyProperties(authRoleDO, authRoleBO);
        String authorities = authRoleDO.getAuthorities();
        if (!StringUtils.isEmpty(authorities)) {
            authRoleBO.setAuthorities(JSONObject.parseArray(authorities, AuthAuthorityBO.class));
        }

        return authRoleBO;
    }

    private List<AuthRoleBO> convertToBOs(List<AuthRoleDO> roleDOList) {
        if (roleDOList == null) {
            return null;
        }

        return roleDOList.stream().map(this::convertToBO).collect(Collectors.toList());
    }

    private QueryParams<AuthRoleDO> convertParam(QueryParams<AuthRoleBO> queryParams) {
        QueryParams<AuthRoleDO> queryParams1 = new QueryParams<>();
        BeanUtils.copyProperties(queryParams, queryParams1);
        queryParams1.setQuery(convertToDO(queryParams.getQuery()));
        return queryParams1;
    }
}
