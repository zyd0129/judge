package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.*;
import com.ps.judge.dao.mapper.*;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.context.RuleContext;
import com.ps.judge.provider.rule.model.ConditionVO;
import com.ps.judge.provider.rule.model.RuleVO;
import com.ps.judge.provider.service.FlowService;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * flow管理服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
@Slf4j
@Service
public class FlowServiceImpl implements FlowService {
    @Autowired
    ConfigFlowMapper configFlowMapper;
    @Autowired
    KSessionManager kSessionManager;
    @Autowired
    ConfigRulePackageMapper configRulePackageMapper;
    @Autowired
    ConfigRulePackageVersionMapper configRulePackageVersionMapper;
    @Autowired
    ConfigRuleMapper configRuleMapper;
    @Autowired
    ConfigRuleConditionMapper configRuleConditionMapper;
    @Autowired
    RuleContext ruleContext;
    @Autowired
    RuleTemplate ruleTemplate;

    @Override
    public ConfigFlowDO getByFlowCode(String flowCode) {
        return this.configFlowMapper.getByFlowCode(flowCode);
    }

    @Override
    public List<ConfigFlowDO> getAllEnable() {
        return  this.configFlowMapper.getAllEnable();
    }

    @Override
    public boolean initFlow() {
        List<ConfigFlowDO> configFlowList = this.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            if (configFlow.getLoadMethod() == 1) {
                continue;
            }
            if (!this.loadFlow(configFlow)) {
                log.error("flow :{}, 加载失败 ", configFlow.getFlowCode());
            }
        }
        return true;
    }

    @Override
    public boolean loadFlow(ConfigFlowDO configFlow) {
        if (configFlow.getLoadMethod() == 0) {
            List<ConfigRuleDO> configRuleList =
                    this.configRuleMapper.listConfigRule(configFlow.getRulePackageVersionId());
            if (configRuleList.isEmpty()) {
                return false;
            }
            List<RuleVO> ruleList = this.getRuleVOList(configRuleList);
            String ruleStr = this.ruleTemplate.build(ruleList);
            return this.ruleContext.add(configFlow.getFlowCode(), ruleStr);
        }
        return this.kSessionManager.addContainer(configFlow);
    }

    @Override
    public boolean removeFlow(ConfigFlowDO configFlow) {
        if (configFlow.getLoadMethod() == 0) {
            return this.ruleContext.remove(configFlow.getFlowCode());
        }
        return this.kSessionManager.removeContainer(configFlow);
    }

    @Override
    public boolean existedFlow(ConfigFlowDO configFlow) {
        return this.ruleContext.existed(configFlow.getFlowCode());
    }

    @Override
    public KieSession getKieSession(String flowCode) {
        return this.ruleContext.getKieSession(flowCode);
    }

    private List<RuleVO> getRuleVOList(List<ConfigRuleDO> configRuleList) {
        List<RuleVO> ruleList = new ArrayList<>(configRuleList.size());
        for (ConfigRuleDO configRule : configRuleList) {
            List<ConfigRuleConditionDO> configRuleConditionList = this.configRuleConditionMapper
                    .getConfigRuleCondition(configRule.getId());
            if (configRuleConditionList.isEmpty()) {
                continue;
            }
            ConfigRulePackageVersionDO configRulePackageVersion =
                    this.configRulePackageVersionMapper.getConfigRulePackageVersionById(configRule.getRulePackageVersionId());
            ConfigRulePackageDO configRulePackage =
                    this.configRulePackageMapper.getConfigRulePackageById(configRulePackageVersion.getRulePackageId());
            List<ConditionVO> conditionList = this.getConditionVOList(configRuleConditionList);
            RuleVO rule = new RuleVO();
            BeanUtils.copyProperties(configRule, rule);
            rule.setRuleCode(configRule.getCode());
            rule.setRuleName(configRule.getName());
            rule.setRuleVersion(String.valueOf(configRule.getVersion()));
            rule.setRuleFlowGroup(configRulePackage.getCode());
            rule.setAgendaGroup(configRulePackage.getCode());
            rule.setRulePackageName(configRulePackage.getName());
            rule.setRulePackageVersion(String.valueOf(configRulePackageVersion.getVersion()));
            rule.setConditionList(conditionList);
            ruleList.add(rule);
        }
        return ruleList;
    }

    private List<ConditionVO> getConditionVOList(List<ConfigRuleConditionDO> configRuleConditionList) {
        List<ConditionVO> conditionList = new ArrayList<>(configRuleConditionList.size());
        for (ConfigRuleConditionDO configRuleCondition : configRuleConditionList) {
            ConditionVO condition = new ConditionVO();
            BeanUtils.copyProperties(configRuleCondition, condition);
            conditionList.add(condition);
        }
        return conditionList;
    }
}
