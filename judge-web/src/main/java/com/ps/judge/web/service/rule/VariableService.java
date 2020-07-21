package com.ps.judge.web.service.rule;

import com.ps.judge.web.pojo.bo.ConfigRuleVariableBO;
import com.ps.judge.web.pojo.query.VariableQuery;

import java.util.List;

public interface VariableService {
    List<ConfigRuleVariableBO> query(VariableQuery variableQuery);
}
