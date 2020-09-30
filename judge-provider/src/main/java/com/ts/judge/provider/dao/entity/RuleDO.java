package com.ts.judge.provider.dao.entity;

import com.ts.judge.provider.enums.RiskCodeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RuleDO {
    private Integer id;
    private Integer rulePackageId;
    private String ruleCode;
    private String ruleName;
    /**
     * 命中结果
     */
    private String riskResult;
    private String expression;

    private String operator;
    private Timestamp gmtCreated;
    private Timestamp gmtModified;
}
