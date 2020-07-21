package com.ps.judge.web.pojo.query;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RuleQuery {
    /**
     * 包名
     */
    private String name;

    @NotNull
    private Integer rulePackageVersionId;

}
