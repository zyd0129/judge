package com.ps.judge.web.auth.req;

import lombok.Data;

@Data
public class AuthUserQueryReq {
    private String fuzzyValue;
    private String role;
}
