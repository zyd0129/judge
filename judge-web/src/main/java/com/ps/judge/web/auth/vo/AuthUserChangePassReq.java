package com.ps.judge.web.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthUserChangePassReq {
    private int id;
    private String prePassword;
    private String rawPassword;
    private String password;

    private String operator;


    private LocalDateTime gmtModified;
}
