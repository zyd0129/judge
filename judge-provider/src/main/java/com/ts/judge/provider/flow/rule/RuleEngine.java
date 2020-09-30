package com.ts.judge.provider.flow.rule;

import java.util.Map;

public interface RuleEngine {
    public RulePackageResult execute(RulePackage rulePackage, Map<String, Object> processParams);
}
