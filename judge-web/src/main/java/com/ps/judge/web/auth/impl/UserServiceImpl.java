package com.ps.judge.web.auth.impl;

import com.ps.judge.dao.entity.AuthUserDO;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.UserService;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public AuthUserBO getByUsername(String username) {
        AuthUserDO authUserDO = userMapper.getByUsername(username);
        return convertToBO(authUserDO);
    }

    private AuthUserBO convertToBO(AuthUserDO authUserDO) {
        if (authUserDO == null) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        BeanUtils.copyProperties(authUserDO, authUserBO);
        return authUserBO;
    }
}
