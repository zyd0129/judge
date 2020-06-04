package com.ps.judge.provider.enums;

public enum StatusEnum {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private int status;
    private String name;

    StatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int status() {
        return this.status;
    }
}
