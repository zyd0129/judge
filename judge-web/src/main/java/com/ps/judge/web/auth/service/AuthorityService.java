package com.ps.judge.web.auth.service;

import com.ps.judge.web.auth.objects.AuthAuthorityBO;

import java.util.List;

public interface AuthorityService {
    List<AuthAuthorityBO> queryAll();
}
