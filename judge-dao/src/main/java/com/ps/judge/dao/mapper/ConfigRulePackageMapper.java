package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigRulePackageDO;
import org.apache.ibatis.annotations.Param;

public interface ConfigRulePackageMapper {

    ConfigRulePackageDO getConfigRulePackageById(@Param("id") int id);
}
