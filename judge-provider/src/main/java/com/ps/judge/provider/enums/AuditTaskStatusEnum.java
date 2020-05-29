package com.ps.judge.provider.enums;

import lombok.Getter;

@Getter
public enum AuditTaskStatusEnum {
    REQUEST_RECEIVED(0, "请求已接收"),
    FORWARDED_SUCCESS(1, "请求转发成功"),
    FORWARDED_FAIL(2, "请求转发失败"),
    VAR_ACCEPTED(3, "变量接收"),
    AUDIT(4, "审核中"),
    AUDIT_COMPLETE(5, "审核完成"),
    CALLBACK(6, "审核完成回调中"),
    CALLBACK_SUCCESS(7, "回调成功"),
    CALLBACK_FAIL(8, "回调失败");

    private int code;
    private String name;

    AuditTaskStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
