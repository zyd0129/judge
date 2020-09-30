package com.ts.judge.provider.service;

import com.ts.judge.provider.flow.rule.RulePackage;
import com.ts.judge.provider.flow.rule.RulePackageResult;

public interface RulePackageService {
    RulePackage getById(Integer id);

    //关联到某个processInstance
    void insert(Integer id, RulePackageResult rulePackageResult);
}
