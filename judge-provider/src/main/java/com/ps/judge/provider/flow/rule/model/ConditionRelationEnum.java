package com.ps.judge.provider.flow.rule.model;

public enum ConditionRelationEnum {
    OR(0, "||"),
    AND(1, ",");

    private int code;
    private String value;

    ConditionRelationEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return this.code;
    }

    public String value() {
        return this.value;
    }
}
