package com.ps.judge.web.auth.impl;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.dao.entity.AuthAuthorityDO;
import com.ps.judge.dao.entity.AuthAuthorityDO;
import com.ps.judge.dao.mapper.AuthorityMapper;
import com.ps.judge.web.auth.AuthorityService;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityMapper authorityMapper;

    @Override
    public List<AuthAuthorityBO> queryAll() {

        List<AuthAuthorityDO> authAuthorityDOList = authorityMapper.queryAll();
        return convertToBOs(authAuthorityDOList);

    }

    private AuthAuthorityBO convertToBO(AuthAuthorityDO authAuthorityDO) {
        if (authAuthorityDO == null) {
            return null;
        }

        AuthAuthorityBO authRoleBO = new AuthAuthorityBO();
        BeanUtils.copyProperties(authAuthorityDO, authRoleBO);
        return authRoleBO;
    }

    private List<AuthAuthorityBO> convertToBOs(List<AuthAuthorityDO> authAuthorityDOList) {
        if (authAuthorityDOList == null) {
            return null;
        }

        return authAuthorityDOList.stream().map(this::convertToBO).collect(Collectors.toList());
    }
}
