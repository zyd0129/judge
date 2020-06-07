package com.ps.judge.web.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import com.ps.judge.dao.mapper.DepartmentMapper;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.service.DepartmentService;
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
    public List<AuthDepartmentBO> query(QueryParams<AuthDepartmentBO> queryParams) {

        QueryParams<AuthDepartmentDO> queryReq = convertParam(queryParams);
        return convertToBOs(departmentMapper.query(queryReq));
    }

    private Set<String> getUsernameSet(AuthDepartmentBO departmentBO) {
        Set<String> curUsernameSet;
        if (departmentBO.getMembers() != null) {
            curUsernameSet = departmentBO.getMembers().stream().map(AuthUserBO::getUsername).distinct().collect(Collectors.toSet());
        } else {
            curUsernameSet = new HashSet<>();
        }
        if (!StringUtils.isEmpty(departmentBO.getPic())) {
            curUsernameSet.add(departmentBO.getPic());
        }
        return curUsernameSet;
    }

    /*
     *
     * @param departmentBO
     */
    @Override
    @Transactional
    public void update(AuthDepartmentBO departmentBO) {
        if (departmentBO == null) {
            return;
        }
        AuthDepartmentBO preDepartmentBO = getById(departmentBO.getId());
        Set<String> preUsernameSet = getUsernameSet(preDepartmentBO);

        departmentMapper.update(convertToDO(departmentBO));

        Set<String> curUsernameSet = getUsernameSet(departmentBO);

        Set<String> incSet = new HashSet<>(curUsernameSet);
        incSet.removeAll(preUsernameSet);
        if (incSet.size() > 0)
            userMapper.batchUpdateDepartment(incSet, departmentBO.getName());
        Set<String> decSet = new HashSet<>(preUsernameSet);
        decSet.removeAll(curUsernameSet);
        if (decSet.size() > 0)
            userMapper.batchUpdateDepartment(decSet, "");

    }


    @Override
    public AuthDepartmentBO getById(int id) {
        return convertToBO(departmentMapper.getById(id));
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        AuthDepartmentBO preDepartmentBO = getById(id);
        Set<String> preUsernameSet = getUsernameSet(preDepartmentBO);
        departmentMapper.delete(id);
        if (preUsernameSet.size() > 0) {
            userMapper.batchUpdateDepartment(preUsernameSet, "");
        }
    }

    @Override
    public void add(AuthDepartmentBO departmentBO) {
        if (departmentBO == null) {
            return;
        }

        departmentMapper.insert(convertToDO(departmentBO));

        Set<String> curUsernameSet = getUsernameSet(departmentBO);

        if (curUsernameSet.size() > 0) {
            userMapper.batchUpdateDepartment(curUsernameSet, departmentBO.getName());
        }

    }


    private AuthDepartmentDO convertToDO(AuthDepartmentBO authDepartmentBO) {
        if (authDepartmentBO == null) {
            return null;
        }
        AuthDepartmentDO authDepartmentDO = new AuthDepartmentDO();
        BeanUtils.copyProperties(authDepartmentBO, authDepartmentDO);
        if (authDepartmentBO.getMembers() != null) {
            /**
             * 如果不加条件, null会变为字符串
             */
            authDepartmentDO.setMembers(JSON.toJSONString(authDepartmentBO.getMembers()));
        }
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

    private QueryParams<AuthDepartmentDO> convertParam(QueryParams<AuthDepartmentBO> queryParams) {
        QueryParams<AuthDepartmentDO> queryParams1 = new QueryParams<>();
        BeanUtils.copyProperties(queryParams, queryParams1);
        queryParams1.setQuery(convertToDO(queryParams.getQuery()));
        return queryParams1;
    }
}
