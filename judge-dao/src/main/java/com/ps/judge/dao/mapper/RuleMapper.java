package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.RuleDO;
import org.apache.ibatis.annotations.Param;

public interface RuleMapper {
    RuleDO getRuleByCode(@Param("code") String code);
}
