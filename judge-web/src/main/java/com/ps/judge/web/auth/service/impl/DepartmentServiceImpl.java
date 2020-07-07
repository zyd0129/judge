package com.ps.judge.web.auth.service.impl;

import com.ps.common.query.DepartmentQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import com.ps.judge.dao.mapper.DepartmentMapper;
import com.ps.judge.dao.mapper.UserMapper;
import com.ps.judge.web.auth.service.DepartmentService;
import com.ps.judge.web.auth.objects.AuthDepartmentBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.utils.BOUtitls;
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
    public List<AuthDepartmentBO> query(QueryParams<DepartmentQuery> queryParams) {

        return convertToBOs(departmentMapper.query(queryParams));
    }

    private Set<Integer> getIdSet(AuthDepartmentBO departmentBO) {
        Set<Integer> curIdSet;
        if (departmentBO.getMembers() != null) {
            curIdSet = departmentBO.getMembers().stream().map(AuthUserBO::getId).distinct().collect(Collectors.toSet());
        } else {
            curIdSet = new HashSet<>();
        }
        AuthUserBO manager = departmentBO.getManager();
        if (manager != null && !StringUtils.isEmpty(manager.getId())) {
            curIdSet.add(manager.getId());
        }
        return curIdSet;
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
        Set<Integer> preIdSet = getIdSet(preDepartmentBO);
        if (preIdSet.size() > 0) {
            userMapper.batchUpdateDepartment(preIdSet, new AuthDepartmentDO());
        }
        AuthDepartmentDO authDepartmentDO = convertToDO(departmentBO);
        departmentMapper.update(authDepartmentDO);

        Set<Integer> curIdSet = getIdSet(departmentBO);
        if (curIdSet.size() > 0) {
            userMapper.batchUpdateDepartment(curIdSet, authDepartmentDO);
        }
    }


    @Override
    public AuthDepartmentBO getById(int id) {
        return convertToBO(departmentMapper.getById(id));
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        AuthDepartmentBO preDepartmentBO = getById(id);
        Set<Integer> idSet = getIdSet(preDepartmentBO);
        departmentMapper.delete(id);
        if (idSet.size() > 0) {
            userMapper.batchUpdateDepartment(idSet, new AuthDepartmentDO());
        }
    }

    @Override
    public void add(AuthDepartmentBO departmentBO) {
        // 增加成员，首先要判断该成员是否有部门，这部分由前端控制，也可有后端控制，departmentId为空才设置，类似乐观锁的机制，然后判断实际修改的条数和要修改的条数是否相等，不相等抛出异常
        if (departmentBO == null) {
            return;
        }
        AuthDepartmentDO authDepartmentDO = convertToDO(departmentBO);
        departmentMapper.insert(authDepartmentDO);

        Set<Integer> idSet = getIdSet(departmentBO);

        if (idSet.size() > 0) {
            // 设置 departmentId, department 冗余信息
            userMapper.batchUpdateDepartment(idSet, authDepartmentDO);
        }

    }

    @Override
    public int total(QueryParams<DepartmentQuery> convertToQueryParam) {
        return departmentMapper.count(convertToQueryParam);
    }

    @Override
    public List<AuthDepartmentBO> all() {
        return convertToBOs(departmentMapper.all());
    }


    private AuthDepartmentDO convertToDO(AuthDepartmentBO authDepartmentBO) {
        if (authDepartmentBO == null) {
            return null;
        }
        AuthDepartmentDO authDepartmentDO = new AuthDepartmentDO();
        BeanUtils.copyProperties(authDepartmentBO, authDepartmentDO);
        AuthUserBO auth = authDepartmentBO.getManager();
        if (auth != null && !StringUtils.isEmpty(auth.getId())) {
            authDepartmentDO.setManagerId(auth.getId());
        }
        return authDepartmentDO;
    }

    private AuthDepartmentBO convertToBO(AuthDepartmentDO authDepartmentDO) {
        if (authDepartmentDO == null) {
            return null;
        }
        AuthDepartmentBO authDepartmentBO = new AuthDepartmentBO();
        BeanUtils.copyProperties(authDepartmentDO, authDepartmentBO);
        if (authDepartmentDO.getMembers() != null) {
            List<AuthUserBO> authUserBOS = BOUtitls.convertToBOs(authDepartmentDO.getMembers());
            authDepartmentBO.setMembers(authUserBOS);
        }
        if (authDepartmentDO.getManager() != null) {
            authDepartmentBO.setManager(BOUtitls.convertToBO(authDepartmentDO.getManager()));
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
