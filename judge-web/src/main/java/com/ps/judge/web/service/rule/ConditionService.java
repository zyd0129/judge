package com.ps.judge.web.service.rule;

import com.github.pagehelper.PageInfo;
import com.ps.judge.web.pojo.bo.ConfigRuleConditionBO;
import com.ps.judge.web.pojo.query.ConditionQuery;

public interface ConditionService {

    void create(ConfigRuleConditionBO ruleBO);

    PageInfo<ConfigRuleConditionBO> queryByPage(ConditionQuery query, int curPage, int pageSize);

    void modify(ConfigRuleConditionBO ruleBO);
}
