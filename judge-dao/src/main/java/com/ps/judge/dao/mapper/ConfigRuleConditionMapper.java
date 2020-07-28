package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRuleConditionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRuleConditionMapper {
    List<ConfigRuleConditionDO> listEnableConfigRuleCondition(@Param("ruleId")int ruleId);

    void update(ConfigRuleConditionDO conditionDO);

    void insert(ConfigRuleConditionDO conditionDO);

    List<ConfigRuleConditionDO> query(ConfigRuleConditionDO conditionDO);
}
