package com.ps.judge.web.auth.impl;

import com.ps.judge.dao.entity.AuthRoleDO;
import com.ps.judge.dao.entity.AuthUserDO;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.UserService;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public AuthUserBO getByUsername(String username) {
        AuthUserDO authUserDO = userMapper.getByUsername(username);
        return convertToBO(authUserDO);
    }

    @Override
    public List<AuthUserBO> query() {
        return convertToBOs(userMapper.queryAll());
    }

    @Override
    public void addUser(AuthUserBO authUserBO) {
        if(authUserBO==null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.insert(authUserDO);
    }

    @Override
    public void modifyUser(AuthUserBO authUserBO) {
        if(authUserBO==null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.update(authUserDO);
    }

    @Override
    public void deleteUser(int id) {
        userMapper.delete(id);
    }

    @Override
    public void changePassword(AuthUserBO authUserBO) {
        if(authUserBO==null) return;
        AuthUserDO authUserDO = convertToDO(authUserBO);
        userMapper.changePassword(authUserDO);
    }

    @Override
    public List<AuthUserBO> queryDepartmentIsEmpty() {
        return convertToBOs(userMapper.queryDepartmentIsEmpty());
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
