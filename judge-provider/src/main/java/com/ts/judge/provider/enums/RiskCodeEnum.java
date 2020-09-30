package com.ts.judge.provider.enums;

import org.apache.commons.lang.StringUtils;

public enum RiskCodeEnum {
    REJECT(0, "拒绝"),
    MANUAL_REVIEW(5, "人工初审"),
    MANUAL_FINAL_REVIEW(10, "人工终审"),
    PASS(15, "通过");

    private int code;
    private String name;

    RiskCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int value() {
        return this.code;
    }

    public static String getAuditCode(int code) {
        for (RiskCodeEnum auditCodeEnum : RiskCodeEnum.values()) {
            if (auditCodeEnum.code == code) {
                return auditCodeEnum.toString();
            }
        }
        return StringUtils.EMPTY;
    }
}
