package com.ps.judge.web.service.rule;

import com.github.pagehelper.PageInfo;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.pojo.req.RulePackageVersionModifyReq;

import java.util.List;

public interface RulePackageVersionService {
    void create(ConfigRulePackageVersionBO rulePackageVersionBO);

    void modify(ConfigRulePackageVersionBO rulePackageVersionBO);
}
