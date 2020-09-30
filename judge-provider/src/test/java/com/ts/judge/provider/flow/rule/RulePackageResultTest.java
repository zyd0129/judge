package com.ts.judge.provider.flow.rule;

import com.alibaba.fastjson.JSON;
import com.ts.judge.provider.enums.RiskCodeEnum;
import org.junit.Test;

import static org.junit.Assert.*;

public class RulePackageResultTest {

    @Test
    public void test() {
        RulePackageResult rulePackageResult = new RulePackageResult();
        rulePackageResult.addRule(new Rule(1, "rule1", "rule1", RiskCodeEnum.MANUAL_FINAL_REVIEW, null));
        rulePackageResult.addRule(new Rule(2, "rule2", "rule2", RiskCodeEnum.REJECT, null));
        rulePackageResult.addRule(new Rule(3, "rule3", "rule3", RiskCodeEnum.MANUAL_REVIEW, null));
        rulePackageResult.result();
        System.out.println(JSON.toJSONString(rulePackageResult));
    }

}