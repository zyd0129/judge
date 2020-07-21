package com.ps.judge.web.service.rule;

import com.github.pagehelper.PageInfo;
import com.ps.common.query.VariableQuery;
import com.ps.judge.web.models.ConfigRuleVariableBO;
import com.ps.judge.web.pojo.bo.ConfigRuleBO;
import com.ps.judge.web.pojo.query.RuleQuery;

import java.util.List;

public interface RuleService {

    void create(ConfigRuleBO ruleBO);

    PageInfo<ConfigRuleBO> queryByPage(RuleQuery query, int curPage, int pageSize);

    void modify(ConfigRuleBO ruleBO);
}
