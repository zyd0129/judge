package com.ps.judge.web.auth.req;

import lombok.Data;

@Data
public class AuthUserLogin {
    private String username;
    private String password;
}
