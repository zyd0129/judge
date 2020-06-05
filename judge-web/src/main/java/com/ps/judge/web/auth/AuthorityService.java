package com.ps.judge.web.auth;

import com.ps.common.query.QueryParams;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;

import java.util.List;

public interface AuthorityService {
    List<AuthAuthorityBO> queryAll();
}
