package com.ts.judge.provider.enums;

import org.apache.commons.lang.StringUtils;

public enum RiskCodeEnum {
    REJECT(0, "REJECT"),
    MANUAL_REVIEW(5, "MANUAL_REVIEW"),
    MANUAL_FINAL_REVIEW(10, "MANUAL_FINAL_REVIEW"),
    PASS(15, "PASS");

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
