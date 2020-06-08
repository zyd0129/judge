package com.ps.judge.provider.enums;

public enum TriggeredResultEnum {
    REJECT(0, "拒绝"),
    MANUAL_REVIEW(1, "人工初审"),
    ;

    private int status;
    private String name;

    TriggeredResultEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int status() {
        return this.status;
    }
}
