package com.ps.judge.web.auth;

import com.ps.common.query.QueryParams;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.req.AuthUserQueryReq;

import java.util.List;

public interface UserService {
    AuthUserBO getByUsername(String username);

    List<AuthUserBO> query(QueryParams<AuthUserQueryReq> queryVo);

    void addUser(AuthUserBO authUserBO);

    void modifyUser(AuthUserBO authUserBO);

    void deleteUser(int id);

    void changePassword(AuthUserBO authUserBO);

    List<AuthUserBO> queryDepartmentIsEmpty();
}
