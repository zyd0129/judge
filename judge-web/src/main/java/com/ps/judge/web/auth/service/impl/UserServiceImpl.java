package com.ps.judge.web.auth.service.impl;

import com.ps.common.query.QueryParams;
import com.ps.common.query.QueryVo;
import com.ps.judge.dao.entity.AuthUserDO;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.service.UserService;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.req.AuthUserLogin;
import com.ps.common.query.UserQuery;
import com.ps.judge.web.auth.utils.JWTHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    KeyPair keyPair;


    @Override
    public AuthUserBO getByUsername(String username) {
        AuthUserDO authUserDO = userMapper.getByUsername(username);
        return convertToBO(authUserDO);
    }

    @Override
    public List<AuthUserBO> query(QueryParams<UserQuery> query) {
        String fuzzyValue = null;
        String role = null;
        if (query.getQuery() != null) {
            if (query.getQuery().getFuzzyValue() != null) {
                fuzzyValue = "%" + query.getQuery().getFuzzyValue() + "%";
                query.getQuery().setFuzzyValue(fuzzyValue);
            }
            if (query.getQuery().getRole() != null) {
                role = "%" + query.getQuery().getRole() + "%";
                query.getQuery().setRole(role);
            }
        }
        return convertToBOs(userMapper.query(query));
    }

    @Override
    public void addUser(AuthUserBO authUserBO) {
        if (authUserBO == null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.insert(authUserDO);
    }

    @Override
    public void modifyUser(AuthUserBO authUserBO) {
        if (authUserBO == null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.update(authUserDO);
    }

    @Override
    public void deleteUser(int id) {
        userMapper.delete(id);
    }

    @Override
    public void changePassword(AuthUserBO authUserBO) {
        if (authUserBO == null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.changePassword(authUserDO);
    }

    @Override
    public List<AuthUserBO> queryDepartmentIsEmpty() {
        return convertToBOs(userMapper.queryDepartmentIsEmpty());
    }

    @Override
    public String login(AuthUserLogin credential) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
        AuthUserBO authUserBO = (AuthUserBO) authentication.getPrincipal();
        return JWTHelper.generateToken(authUserBO, keyPair.getPrivate(), 6000);
    }

    @Override
    public int total(QueryParams<UserQuery> query) {
        return userMapper.total(query);
    }

    private AuthUserDO convertToDO(AuthUserBO authUserBO) {
        if (authUserBO == null) {
            return null;
        }
        AuthUserDO authUserDO = new AuthUserDO();
        BeanUtils.copyProperties(authUserBO, authUserDO);
        return authUserDO;
    }

    private AuthUserBO convertToBO(AuthUserDO authUserDO) {
        if (authUserDO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserDO, authUserBO);
        return authUserBO;
    }

    private List<AuthUserBO> convertToBOs(List<AuthUserDO> authUserDOList) {
        if (authUserDOList == null) {
            return null;
        }

        return authUserDOList.stream().map(this::convertToBO).collect(Collectors.toList());
    }
}
