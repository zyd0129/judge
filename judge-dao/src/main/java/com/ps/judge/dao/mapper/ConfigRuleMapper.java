package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRuleMapper {

    List<ConfigRuleDO> listConfigRule(@Param("rulePackageVersionId")int rulePackageVersionId);

    void insert(ConfigRuleDO ruleDO);

    List<ConfigRuleDO> query(ConfigRuleDO ruleDO);

    void update(ConfigRuleDO ruleDO);
}
