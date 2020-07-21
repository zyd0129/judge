package com.ps.judge.dao.mapper;


import com.ps.judge.dao.entity.ConfigRuleVariableDO;

import java.util.List;

public interface ConfigRuleVariableMapper {
    List<ConfigRuleVariableDO> query(ConfigRuleVariableDO variableDO);
}
