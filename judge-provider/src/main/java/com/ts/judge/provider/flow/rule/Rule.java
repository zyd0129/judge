package com.ts.judge.provider.flow.rule;

import com.ts.judge.provider.enums.RiskCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
    private Integer id;
    private String ruleCode;
    private String ruleName;
    /**
     * 命中结果
     */
    private RiskCodeEnum riskResult;
    private String expression;
}
