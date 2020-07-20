package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRuleMapper {

    List<ConfigRuleDO> listConfigRule(@Param("rulePackageId")int rulePackageId);
}
