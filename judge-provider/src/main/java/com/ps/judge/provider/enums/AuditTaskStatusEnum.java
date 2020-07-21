package com.ps.judge.provider.enums;

public enum AuditTaskStatusEnum {
    REQUEST_RECEIVED(0, "请求已接收"),
    FORWARDED_SUCCESS(1, "请求转发成功"),
    FORWARDED_FAIL(2, "请求转发失败"),
    VAR_ACCEPTED_SUCCESS(3, "变量接受成功"),
    VAR_COMPUTE_FAIL(4, "变量计算失败"),
    AUDIT(5, "审核中"),
    AUDIT_COMPLETE_SUCCESS(6, "审核成功"),
    AUDIT_COMPLETE_FAIL(7, "审核失败"),
    CALLBACK(8, "审核完成回调中"),
    CALLBACK_SUCCESS(9, "回调成功"),
    CALLBACK_FAIL(10, "回调失败");

    private int code;
    private String name;

    AuditTaskStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int value() {
        return this.code;
    }
}
