package com.ts.judge.provider.dao.ddao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRulePackageVersionMapper {
    ConfigRulePackageVersionDO getConfigRulePackageVersionById(@Param("id") int id);

    void insert(ConfigRulePackageVersionDO versionDO);

    void update(ConfigRulePackageVersionDO versionDO);

    List<ConfigRulePackageVersionDO> queryByPackageId(@Param("rulePackageId") Integer rulePackageId);
}
