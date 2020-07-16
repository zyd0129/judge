package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRuleVariableDO;

import java.util.List;

public interface ConfigRuleVariableMapper {
    List<ConfigRuleVariableDO> all();

    List<ConfigRuleVariableDO> query(ConfigRuleVariableDO queryDO);
}
