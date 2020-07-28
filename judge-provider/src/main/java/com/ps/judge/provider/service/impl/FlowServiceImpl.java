package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.*;
import com.ps.judge.dao.mapper.*;
import com.ps.judge.provider.enums.StatusEnum;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.context.RuleContext;
import com.ps.judge.provider.rule.executor.RuleExecutor;
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
import java.util.Objects;

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
    ConfigRulePackageMapper configRulePackageMapper;
    @Autowired
    ConfigFlowRulePackageMapper configFlowRulePackageMapper;
    @Autowired
    ConfigRulePackageVersionMapper configRulePackageVersionMapper;
    @Autowired
    ConfigRuleMapper configRuleMapper;
    @Autowired
    ConfigRuleConditionMapper configRuleConditionMapper;
    @Autowired
    RuleTemplate ruleTemplate;
    @Autowired
    RuleContext ruleContext;
    @Autowired
    RuleExecutor ruleExecutor;

    @Override
    public ConfigFlowDO getByFlowCode(String flowCode) {
        return this.configFlowMapper.getByFlowCode(flowCode);
    }

    @Override
    public List<ConfigFlowRulePackageDO> getConfigFlowRulePackageList(String flowCode) {
        return this.configFlowRulePackageMapper.listConfigFlowRulePackageByFlowCode(flowCode);
    }

    @Override
    public ConfigRulePackageDO getConfigRulePackage(int rulePackageVersionId) {
        ConfigRulePackageVersionDO configRulePackageVersion =
                this.configRulePackageVersionMapper.getConfigRulePackageVersionById(rulePackageVersionId);
        if (Objects.isNull(configRulePackageVersion)) {
            return null;
        }
        return this.configRulePackageMapper.getConfigRulePackageById(configRulePackageVersion.getRulePackageId());
    }

    @Override
    public boolean initFlow() {
        List<ConfigFlowDO> configFlowList = this.configFlowMapper.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            if (!this.loadFlow(configFlow)) {
                log.error("flow :{}, 加载失败 ", configFlow.getFlowCode());
            }
        }
        return true;
    }

    @Override
    public boolean loadFlow(ConfigFlowDO configFlow) {
        if (configFlow.getStatus() == StatusEnum.DISABLE.value()) {
            return false;
        }
        List<ConfigFlowRulePackageDO> configFlowRulePackageList =
                this.getConfigFlowRulePackageList(configFlow.getFlowCode());
        if (configFlowRulePackageList.isEmpty()) {
            return false;
        }

        List<ConfigRuleDO> allConfigRuleList = new ArrayList<>();
        for (ConfigFlowRulePackageDO configFlowRulePackage : configFlowRulePackageList) {
            List<ConfigRuleDO> configRuleList =
                    this.configRuleMapper.listEnableConfigRule(configFlowRulePackage.getRulePackageVersionId());
            allConfigRuleList.addAll(configRuleList);
        }
        if (allConfigRuleList.isEmpty()) {
            return false;
        }

        List<RuleVO> ruleList = this.getRuleVOList(allConfigRuleList);
        String ruleStr = this.ruleTemplate.build(ruleList);
        return this.ruleContext.add(configFlow.getFlowCode(), ruleStr);
    }

    private List<RuleVO> getRuleVOList(List<ConfigRuleDO> configRuleList) {
        List<RuleVO> ruleList = new ArrayList<>(configRuleList.size());
        for (ConfigRuleDO configRule : configRuleList) {
            List<ConfigRuleConditionDO> configRuleConditionList =
                    this.configRuleConditionMapper.listEnableConfigRuleCondition(configRule.getId());
            if (configRuleConditionList.isEmpty()) {
                continue;
            }

            ConfigRulePackageVersionDO configRulePackageVersion =
                    this.configRulePackageVersionMapper.getConfigRulePackageVersionById(configRule.getRulePackageVersionId());
            ConfigRulePackageDO configRulePackage =
                    this.configRulePackageMapper.getConfigRulePackageById(configRulePackageVersion.getRulePackageId());

            List<ConditionVO> conditionList = this.getConditionVOList(configRule.getConditionRelation(), configRuleConditionList);
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

    private List<ConditionVO> getConditionVOList(int conditionRelation, List<ConfigRuleConditionDO> configRuleConditionList) {
        List<ConditionVO> conditionList = new ArrayList<>(configRuleConditionList.size());
        for (ConfigRuleConditionDO configRuleCondition : configRuleConditionList) {
            ConditionVO condition = new ConditionVO();
            BeanUtils.copyProperties(configRuleCondition, condition);
            condition.setRelation(conditionRelation);
            conditionList.add(condition);
        }
        return conditionList;
    }

    @Override
    public boolean removeFlow(String flowCode) {
        return this.ruleContext.remove(flowCode);
    }

    @Override
    public boolean existedFlow(String flowCode) {
        ConfigFlowDO configFlow = this.getByFlowCode(flowCode);
        if (Objects.isNull(configFlow)) {
            return false;
        }
        if (configFlow.getStatus() == StatusEnum.DISABLE.value()) {
            return false;
        }
        return this.ruleContext.existed(flowCode);
    }

    @Override
    public boolean executorFlow(String flowCode, List paramList) {
        if (!this.existedFlow(flowCode)) {
            return false;
        }
        List<ConfigFlowRulePackageDO> configFlowRulePackageList = this.getConfigFlowRulePackageList(flowCode);
        if (configFlowRulePackageList.isEmpty()) {
            return false;
        }
        KieSession kieSession = this.ruleContext.getKieSession(flowCode);
        if (Objects.isNull(kieSession)) {
            return false;
        }
        for (ConfigFlowRulePackageDO configFlowRulePackage : configFlowRulePackageList) {
            ConfigRulePackageDO configRulePackage =
                    this.getConfigRulePackage(configFlowRulePackage.getRulePackageVersionId());
            this.ruleExecutor.executor(kieSession, paramList, configRulePackage.getCode());
        }
        return true;
    }

}
