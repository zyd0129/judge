package com.ts.judge.provider.service.impl;

import com.ts.judge.provider.dao.entity.RuleDO;
import com.ts.judge.provider.dao.entity.RulePackageDO;
import com.ts.judge.provider.dao.entity.RuleTriggeredDO;
import com.ts.judge.provider.dao.mapper.RulePackageMapper;
import com.ts.judge.provider.enums.RiskCodeEnum;
import com.ts.judge.provider.flow.rule.Rule;
import com.ts.judge.provider.flow.rule.RulePackage;
import com.ts.judge.provider.flow.rule.RulePackageResult;
import com.ts.judge.provider.service.RulePackageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RulePackageServiceImpl implements RulePackageService {

    @Autowired
    RulePackageMapper rulePackageMapper;

    private RulePackage convertToBO(RulePackageDO rulePackageDO) {
        if (rulePackageDO == null) {
            return null;
        }
        RulePackage rulePackage = new RulePackage();
        BeanUtils.copyProperties(rulePackageDO, rulePackage);
        List<RuleDO> ruleDOList = rulePackageDO.getRuleDOList();
        if (ruleDOList != null) {
            List<Rule> ruleList = ruleDOList.stream().map(this::convertToBO).collect(Collectors.toList());
            rulePackage.setRuleList(ruleList);
        }
        return rulePackage;
    }

    private Rule convertToBO(RuleDO ruleDO) {
        if (ruleDO == null) {
            return null;
        }
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleDO, rule);
        rule.setRiskResult(RiskCodeEnum.valueOf(ruleDO.getRiskResult()));
        return rule;
    }

    @Override
    public RulePackage getById(Integer id) {
        return convertToBO(rulePackageMapper.getById(id));
    }

    @Override
    public void insert(Integer id, RulePackageResult rulePackageResult) {
        if (rulePackageResult == null) {
            return;
        }
        List<RuleTriggeredDO> ruleTriggeredDOList = rulePackageResult.getDetails().stream().map(rule -> {
            RuleTriggeredDO ruleTriggeredDO = new RuleTriggeredDO();
            ruleTriggeredDO.setRuleId(rule.getId());
            ruleTriggeredDO.setProcessInstanceId(id);
            return ruleTriggeredDO;
        }).collect(Collectors.toList());
        if (ruleTriggeredDOList.size() > 0) {
            rulePackageMapper.batchInsert(ruleTriggeredDOList);
        }
    }
}
