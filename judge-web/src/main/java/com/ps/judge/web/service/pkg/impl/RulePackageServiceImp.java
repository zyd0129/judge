package com.ps.judge.web.service.pkg.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ps.judge.dao.entity.ConfigRulePackageDO;
import com.ps.judge.dao.mapper.ConfigRulePackageMapper;
import com.ps.judge.dao.mapper.ConfigRulePackageVersionSequenceMapper;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.query.RulePackageQuery;
import com.ps.judge.web.service.pkg.RulePackageService;
import com.ps.judge.web.util.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RulePackageServiceImp implements RulePackageService {
    @Autowired
    ConfigRulePackageMapper rulePackageMapper;

    @Autowired
    ConfigRulePackageVersionSequenceMapper versionSequenceMapper;

    @Override
    public List<ConfigRulePackageBO> query(RulePackageQuery rulePackageQuery) {
        ConfigRulePackageDO configRulePackageDO = convertQoToDo(rulePackageQuery);
        List<ConfigRulePackageDO> rulePackageDOList = rulePackageMapper.query(configRulePackageDO);
        return rulePackageDOList.stream().map(ConfigRulePackageBO::convertFromDo).collect(Collectors.toList());
    }

    @Override
    public PageInfo<ConfigRulePackageBO> queryByPage(RulePackageQuery rulePackageQuery, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        ConfigRulePackageDO configRulePackageDO = convertQoToDo(rulePackageQuery);
        Page<ConfigRulePackageDO> rulePackageDOPage = (Page<ConfigRulePackageDO>) rulePackageMapper.query(configRulePackageDO);
        return PageUtils.convertPage(rulePackageDOPage, ConfigRulePackageBO::convertFromDo);
    }

    @Override
    @Transactional
    public void create(ConfigRulePackageBO rulePackageBO) {
        ConfigRulePackageDO rulePackageDO = rulePackageBO.convertToDo();
        rulePackageMapper.insert(rulePackageDO);
        rulePackageBO.setId(rulePackageDO.getId());
        //维护递增版本号
        versionSequenceMapper.insertByPackageId(rulePackageDO.getId());
    }

    @Override
    public void modify(ConfigRulePackageBO rulePackageBO) {
        ConfigRulePackageDO rulePackageDO = rulePackageBO.convertToDo();
        rulePackageMapper.update(rulePackageDO);
    }

    private ConfigRulePackageDO convertQoToDo(RulePackageQuery rulePackageQuery) {
        ConfigRulePackageDO configRulePackageDO = new ConfigRulePackageDO();
        if (rulePackageQuery != null) {
            BeanUtils.copyProperties(rulePackageQuery, configRulePackageDO);
        }
        return configRulePackageDO;
    }
}
