package com.ps.judge.web.auth;

import com.ps.judge.web.auth.objects.AuthUserBO;

public interface UserService {
    AuthUserBO getByUsername(String username);
}
