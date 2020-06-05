package com.ps.judge.web.auth.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import com.ps.judge.dao.mapper.DepartmentMapper;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.DepartmentService;
import com.ps.judge.web.auth.objects.AuthDepartmentBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public List<AuthDepartmentBO> query() {

        return convertToBOs(departmentMapper.queryAll());
    }

    @Override
    @Transactional
    public void update(AuthDepartmentBO departmentBO) {
        if (departmentBO == null) {
            return;
        }
        departmentMapper.update(convertToDO(departmentBO));
        if(departmentBO.getMembers().size()>0 || !StringUtils.isEmpty(departmentBO.getPic())){
            Set<String> usernameSet =  departmentBO.getMembers().stream().map(AuthUserBO::getUsername).collect(Collectors.toSet());
            usernameSet.add(departmentBO.getPic());
            userMapper.batchUpdateDepartment(usernameSet,departmentBO.getName());
        }

    }

    private AuthDepartmentDO convertToDO(AuthDepartmentBO authDepartmentBO) {
        if (authDepartmentBO == null) {
            return null;
        }
        AuthDepartmentDO authDepartmentDO = new AuthDepartmentDO();
        BeanUtils.copyProperties(authDepartmentBO, authDepartmentDO);
        authDepartmentDO.setMembers(JSON.toJSONString(authDepartmentBO.getMembers()));
        return authDepartmentDO;
    }

    private AuthDepartmentBO convertToBO(AuthDepartmentDO authDepartmentDO) {
        if (authDepartmentDO == null) {
            return null;
        }
        AuthDepartmentBO authDepartmentBO = new AuthDepartmentBO();
        BeanUtils.copyProperties(authDepartmentDO, authDepartmentBO);
        if (!StringUtils.isEmpty(authDepartmentDO.getMembers())) {
            authDepartmentBO.setMembers(new HashSet<>(JSONObject.parseArray(authDepartmentDO.getMembers(), AuthUserBO.class)));
        }
        return authDepartmentBO;
    }

    private List<AuthDepartmentBO> convertToBOs(List<AuthDepartmentDO> authDepartmentDOList) {
        if (authDepartmentDOList == null) {
            return null;
        }

        return authDepartmentDOList.stream().map(this::convertToBO).collect(Collectors.toList());
    }
}
