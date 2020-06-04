package com.ps.judge.web.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUserResetPassReq {
    private int id;
    private String rawPassword;
    private String password;

    private String operator;


    private LocalDateTime gmtModified;
}
