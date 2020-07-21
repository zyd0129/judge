package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.*;
import com.ps.judge.dao.mapper.*;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.rule.builder.DroolsRuleTemplate;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.manager.RuleManager;
import com.ps.judge.provider.rule.model.ConditionVO;
import com.ps.judge.provider.rule.model.RuleVO;
import com.ps.judge.provider.service.FlowService;
import org.apache.ibatis.annotations.Param;
import org.kie.api.runtime.KieSession;
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
    RuleManager ruleManager;

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
        RuleTemplate ruleTemplate = new DroolsRuleTemplate();
        List<ConfigFlowDO> configFlowList = this.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            if (configFlow.getLoadMethod() == 1) {
                continue;
            }
            List<ConfigRuleDO> configRuleList = this.configRuleMapper.listConfigRule(configFlow.getRulePackageVersionId());
            if (configRuleList.isEmpty()) {
                continue;
            }
            List<RuleVO> ruleList = this.getRuleVOList(configRuleList);
            String ruleStr = ruleTemplate.build(ruleList);
            System.err.println(ruleStr);
            this.ruleManager.add(configFlow.getFlowCode(), ruleStr);
        }
        return true;
    }

    private List<RuleVO> getRuleVOList(List<ConfigRuleDO> configRuleList) {
        List<RuleVO> ruleList = new ArrayList<>(configRuleList.size());
        for (ConfigRuleDO configRule : configRuleList) {
            List<ConfigRuleConditionDO> configRuleConditionList = this.configRuleConditionMapper
                    .getConfigRuleCondition(configRule.getId());
            if (configRuleConditionList.isEmpty()) {
                continue;
            }
            List<ConditionVO> conditionList = this. getConditionVOList(configRuleConditionList);

            ConfigRulePackageVersionDO configRulePackageVersionDO = this.configRulePackageVersionMapper
                    .getConfigRulePackageVersionById(configRule.getRulePackageVersionId());
            ConfigRulePackageDO configRulePackage = this.configRulePackageMapper
                    .getConfigRulePackageById(configRulePackageVersionDO.getPackageId());

            RuleVO rule = new RuleVO();
            rule.setRuleCode(configRule.getCode());
            rule.setRuleName(configRule.getName());
            rule.setRuleVersion(String.valueOf(configRule.getVersion()));
            rule.setRuleFlowGroup(configRulePackage.getCode());
            rule.setAgendaGroup(configRulePackage.getCode());
            rule.setSalience(configRule.getSalience());
            rule.setScore(configRule.getScore());
            rule.setResult(configRule.getResult());
            rule.setConditionList(conditionList);
            ruleList.add(rule);
        }
        return ruleList;
    }

    private List<ConditionVO> getConditionVOList(List<ConfigRuleConditionDO> configRuleConditionList) {
        List<ConditionVO> conditionList = new ArrayList<>(configRuleConditionList.size());
        for (ConfigRuleConditionDO configRuleCondition : configRuleConditionList) {
            ConditionVO condition = new ConditionVO();
            condition.setOperator(configRuleCondition.getOperator());
            condition.setOperand(configRuleCondition.getOperand());
            condition.setVariableCode(configRuleCondition.getVariableCode());
            condition.setVariableType(configRuleCondition.getVariableType());
            conditionList.add(condition);
        }
        return conditionList;
    }

    @Override
    public boolean loadFlow(ConfigFlowDO configFlow) {
        return this.kSessionManager.addContainer(configFlow);
    }

    @Override
    public boolean removeFlow(ConfigFlowDO configFlow) {
        return this.kSessionManager.removeContainer(configFlow);
    }

    @Override
    public KieSession getKieSession(String flowCode) {
        return this.ruleManager.getKieSession(flowCode);
    }
}
