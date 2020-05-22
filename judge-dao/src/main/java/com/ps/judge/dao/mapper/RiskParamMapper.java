package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.RiskParamDO;
import org.apache.ibatis.annotations.Param;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/21
 */
public interface RiskParamMapper {
    RiskParamDO getRiskParamByTenantIdAndApplyId(@Param("tenantId") String tenantId, @Param("applyId") String applyId);

    int insert(RiskParamDO riskParam);
}
