package com.ps.judge.provider.initialization;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.entity.ConfigRuleConditionDO;
import com.ps.judge.dao.entity.ConfigRuleDO;
import com.ps.judge.dao.entity.ConfigRulePackageDO;
import com.ps.judge.dao.mapper.ConfigRuleConditionMapper;
import com.ps.judge.dao.mapper.ConfigRuleMapper;
import com.ps.judge.dao.mapper.ConfigRulePackageMapper;
import com.ps.judge.provider.drools.KieContainerInitialization;
import com.ps.judge.provider.rule.builder.DroolsRuleTemplate;
import com.ps.judge.provider.rule.builder.RuleTemplate;
import com.ps.judge.provider.rule.manager.RuleManager;
import com.ps.judge.provider.rule.model.ConditionVO;
import com.ps.judge.provider.rule.model.RuleVO;
import com.ps.judge.provider.service.ConfigFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务初始化时，将数据加载到各个Context中
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
@Component
@Slf4j
public class JudgeProviderInitialization {
    @Autowired
    KieContainerInitialization kieContainerInitialization;
    @Autowired
    ConfigFlowService configFlowService;
    @Autowired
    ConfigRulePackageMapper configRulePackageMapper;
    @Autowired
    ConfigRuleMapper configRuleMapper;
    @Autowired
    ConfigRuleConditionMapper configRuleConditionMapper;
    @Autowired
    RuleManager ruleManager;

    @PostConstruct
    public void init() {
        log.info("start provider initiating ");
        long startTime = System.currentTimeMillis();

        this.kieContainerInitialization.init();
        this.initFlow();
        long endTime = System.currentTimeMillis();
        log.info("end provider initiating , time cost: {}", endTime - startTime);
    }

    private void initFlow() {
        RuleTemplate ruleTemplate = new DroolsRuleTemplate();
        List<ConfigFlowDO> configFlowList = this.configFlowService.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            if (configFlow.getLoadMethod() == 1) {
                continue;
            }
            List<ConfigRuleDO> configRuleList = this.configRuleMapper.listConfigRule(configFlow.getRulePackageId());
            if (configRuleList.isEmpty()) {
                continue;
            }
            List<RuleVO> ruleList = this.getRuleVOList(configRuleList);
            String ruleStr = ruleTemplate.build(ruleList);
            this.ruleManager.add(configFlow.getFlowCode(), ruleStr);
        }
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
            ConfigRulePackageDO configRulePackage = this.configRulePackageMapper
                    .getConfigRulePackageById(configRule.getRulePackageId());
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

}
