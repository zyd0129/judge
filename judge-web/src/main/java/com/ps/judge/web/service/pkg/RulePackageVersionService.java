package com.ps.judge.web.service.pkg;

import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;

public interface RulePackageVersionService {
    void create(ConfigRulePackageVersionBO rulePackageVersionBO);

    void modify(ConfigRulePackageVersionBO rulePackageVersionBO);
}
