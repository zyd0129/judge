package com.ps.judge.web.service.rule;

import com.ps.common.query.VariableQuery;
import com.ps.judge.web.models.ConfigRuleVariableBO;

import java.util.List;

public interface VariableService {
    List<ConfigRuleVariableBO> all();

    List<ConfigRuleVariableBO> query(VariableQuery variableQuery);
}
