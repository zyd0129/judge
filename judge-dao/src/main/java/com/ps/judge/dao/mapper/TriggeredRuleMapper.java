package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.TriggeredRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TriggeredRuleMapper {
    List<TriggeredRuleDO> listTriggeredRuleLogByApplyId(@Param("applyId") String applyId);
    int insert(TriggeredRuleDO triggeredRuleLog);
}
