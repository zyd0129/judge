package com.ps.judge.web.service.rule;

import com.github.pagehelper.PageInfo;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;

import java.util.List;

public interface RulePackageService {

    List<ConfigRulePackageBO> query(RulePackageQuery rulePackageQuery);

    PageInfo<ConfigRulePackageBO> queryByPage(RulePackageQuery rulePackageQuery, int curPage, int pageSize);

    void create(ConfigRulePackageBO rulePackageBO);

    void modify(ConfigRulePackageBO rulePackageBO);
}
