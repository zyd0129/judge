package com.ts.judge.provider.flow.rule;

import com.ts.judge.provider.enums.RiskCodeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RulePackageResult {
    private RiskCodeEnum result;
    private List<Rule> details;

    public void addRule(Rule rule) {
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(rule);
    }

    public void result() {
        if (details == null) {
            details = new ArrayList<>();
        }
        Rule defaultR = new Rule();
        defaultR.setRiskResult(RiskCodeEnum.PASS);
        Rule ruleResult = details.stream().min((a, b) -> a.getRiskResult().value() - b.getRiskResult().value()).orElse(defaultR);
        result = ruleResult.getRiskResult();
    }
}
