package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.RiskDO;
import org.apache.ibatis.annotations.Param;

public interface RiskMapper {
    RiskDO getRiskByTenantIdAndApplyId(@Param("tenantId") String tenantId, @Param("applyId") String applyId);
    int insert(RiskDO risk);
}
