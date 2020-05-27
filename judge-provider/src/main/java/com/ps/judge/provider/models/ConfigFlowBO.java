package com.ps.judge.provider.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfigFlowBO {
    private Integer id;
    private String flowCode;
    private String flowName;
    private String tenantCode;
    private String productCode;
    private Integer curPackageId;
    private String curPackageGroup;
    private String curPackageArtifact;
    private String curPackageVersion;
    private Status status=Status.STOPPED;
    private String operator;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public String  getReleaseId(){
        return curPackageGroup+":"+ curPackageArtifact +":"+curPackageVersion;
    }

    public enum Status {
        STOPPED("STOPPED", 0), STARTED("STARTED", 1), DISABLED("DISABLED", 2);
        // 成员变量
        private String name;
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        // 构造方法
        private Status(String name, int value) {
            this.name = name;
            this.value = value;
        }
        // 普通方法
        public static Status valueOf(int value) {
            for (Status c : Status.values()) {
                if (c.getValue() == value) {
                    return c;
                }
            }
            return null;
        }
        // get set 方法
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
