package com.ps.judge.dao.mapper;

import com.ps.judge.dao.entity.ConfigFlowRulePackageDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigFlowRulePackageMapper {
    List<ConfigFlowRulePackageDO> listConfigFlowRulePackageByFlowCode(@Param("flowCode") String flowCode);
}
