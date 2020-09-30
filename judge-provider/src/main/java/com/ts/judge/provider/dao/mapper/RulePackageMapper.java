package com.ts.judge.provider.dao.mapper;

import com.ts.judge.provider.dao.entity.RulePackageDO;
import com.ts.judge.provider.dao.entity.RuleTriggeredDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RulePackageMapper {
    RulePackageDO getById(@Param("id") Integer id);

    void batchInsert(List<RuleTriggeredDO> ruleTriggeredDOList);
}
