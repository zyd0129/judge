package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRuleConditionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRuleConditionMapper {
    List<ConfigRuleConditionDO> getConfigRuleCondition(@Param("ruleId")int ruleId);
}
