package com.ps.judge.provider.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private int status;
    private String name;

    StatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }
}
