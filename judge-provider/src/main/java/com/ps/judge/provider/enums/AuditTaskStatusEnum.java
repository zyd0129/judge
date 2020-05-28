package com.ps.judge.provider.enums;

import lombok.Getter;

@Getter
public enum AuditTaskStatusEnum {

    ;

    private int code;
    private String name;

    AuditTaskStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
