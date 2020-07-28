package com.ps.judge.web.service.pkg.impl;

import com.ps.judge.dao.entity.ConfigRulePackageVersionDO;
import com.ps.judge.dao.mapper.ConfigRulePackageVersionMapper;
import com.ps.judge.dao.mapper.ConfigRulePackageVersionSequenceMapper;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import com.ps.judge.web.service.pkg.RulePackageVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RulePackageVersionServiceImpl implements RulePackageVersionService {

    @Autowired
    ConfigRulePackageVersionMapper versionMapper;

    @Autowired
    ConfigRulePackageVersionSequenceMapper sequenceMapper;

    @Override
    @Transactional
    public void create(ConfigRulePackageVersionBO rulePackageVersionBO) {
        //** 这行加锁了
        int sequence = sequenceMapper.getSequenceByPackageId(rulePackageVersionBO.getRulePackageId());
        rulePackageVersionBO.setVersion(sequence + 1);
        //设置为未启用
        rulePackageVersionBO.setStatus(0);
        ConfigRulePackageVersionDO versionDO = rulePackageVersionBO.convertToDo();
        versionMapper.insert(versionDO);
        sequenceMapper.setSequenceByPackageId(rulePackageVersionBO.getRulePackageId(), sequence + 1);
        rulePackageVersionBO.setId(versionDO.getId());
    }

    @Override
    public void modify(ConfigRulePackageVersionBO rulePackageVersionBO) {

        ConfigRulePackageVersionDO versionDO = rulePackageVersionBO.convertToDo();
        versionMapper.update(versionDO);
    }
}
