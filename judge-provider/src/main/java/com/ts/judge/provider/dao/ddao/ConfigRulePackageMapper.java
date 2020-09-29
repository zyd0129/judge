package com.ts.judge.provider.dao.ddao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRulePackageMapper {

    ConfigRulePackageDO getConfigRulePackageById(@Param("id") int id);

    void insert(ConfigRulePackageDO rulePackageDO);

    List<ConfigRulePackageDO> query(ConfigRulePackageDO configRulePackageDO);

    void update(ConfigRulePackageDO rulePackageDO);
}
