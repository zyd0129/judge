package com.ps.judge.provider.enums;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public enum AuditCodeEnum {
    REJECT(0, "拒绝"),
    MANUAL_REVIEW(1, "人工初审"),
    MANUAL_FINAL_REVIEW(2, "人工终审"),
    PASS(3, "通过");

    private int code;
    private String name;

    AuditCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getAuditCode(int code) {
        for (AuditCodeEnum auditCodeEnum : AuditCodeEnum.values()) {
            if (auditCodeEnum.code == code) {
                return auditCodeEnum.toString();
            }
        }
        return StringUtils.EMPTY;
    }
}
