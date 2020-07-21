package com.ps.judge.web.pojo.query;

import lombok.Data;

@Data
public class RulePackageQuery {
    /**
     * 包名
     */
    private String name;

    private String tenantCode;
}
