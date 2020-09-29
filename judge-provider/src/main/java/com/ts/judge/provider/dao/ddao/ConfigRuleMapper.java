package com.ts.judge.provider.dao.ddao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRuleMapper {

    List<ConfigRuleDO> listEnableConfigRule(@Param("rulePackageVersionId")int rulePackageVersionId);

    void insert(ConfigRuleDO ruleDO);

    List<ConfigRuleDO> query(ConfigRuleDO ruleDO);

    void update(ConfigRuleDO ruleDO);
}
