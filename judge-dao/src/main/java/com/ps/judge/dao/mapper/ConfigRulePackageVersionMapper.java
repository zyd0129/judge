package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRulePackageVersionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigRulePackageVersionMapper {
    void insert(ConfigRulePackageVersionDO versionDO);

    void update(ConfigRulePackageVersionDO versionDO);

    List<ConfigRulePackageVersionDO> queryByPackageId(@Param("packageId") Integer packageId);
}
