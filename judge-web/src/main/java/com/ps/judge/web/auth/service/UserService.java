package com.ps.judge.web.auth.service;

import com.ps.common.query.QueryParams;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.req.AuthUserLogin;
import com.ps.common.query.UserQuery;

import java.util.List;

public interface UserService {
    AuthUserBO getByUsername(String username);

    List<AuthUserBO> query(QueryParams<UserQuery> queryVo);

    void addUser(AuthUserBO authUserBO);

    void modifyUser(AuthUserBO authUserBO);

    void deleteUser(int id);

    void changePassword(AuthUserBO authUserBO);

    List<AuthUserBO> queryDepartmentIsEmpty();

    String login(AuthUserLogin credential);

    int total(QueryParams<UserQuery> queryVo);
}
