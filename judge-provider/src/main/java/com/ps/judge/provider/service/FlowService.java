package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.entity.ConfigFlowRulePackageDO;
import com.ps.judge.dao.entity.ConfigRulePackageDO;

import java.util.List;

/**
 * flow管理服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
public interface FlowService {
    ConfigFlowDO getByFlowCode(String flowCode);

    List<ConfigFlowRulePackageDO> getConfigFlowRulePackageList(String flowCode);

    ConfigRulePackageDO getConfigRulePackage(int rulePackageVersionId);

    boolean initFlow();

    boolean loadFlow(ConfigFlowDO configFlow);

    boolean removeFlow(String flowCode);

    boolean existedFlow(String flowCode);

    boolean executorFlow(String flowCode, List paramList);
}
