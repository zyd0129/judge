package com.ps.judge.web.auth;

import com.ps.judge.web.auth.objects.AuthUserBO;

import java.util.List;

public interface UserService {
    AuthUserBO getByUsername(String username);

    List<AuthUserBO> query();

    void addUser(AuthUserBO authUserBO);

    void modifyUser(AuthUserBO authUserBO);

    void deleteUser(int id);

    void changePassword(AuthUserBO authUserBO);

    List<AuthUserBO> queryDepartmentIsEmpty();
}
