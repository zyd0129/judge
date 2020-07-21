package com.ps.judge.web.service.rule.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ps.judge.dao.entity.ConfigRuleDO;
import com.ps.judge.dao.mapper.ConfigRuleMapper;
import com.ps.judge.web.pojo.bo.ConfigRuleBO;
import com.ps.judge.web.pojo.query.RuleQuery;
import com.ps.judge.web.service.rule.RuleService;
import com.ps.judge.web.util.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    ConfigRuleMapper ruleMapper;

    @Override
    public void create(ConfigRuleBO ruleBO) {
        ConfigRuleDO ruleDO = ruleBO.convertToDo();
        ruleMapper.insert(ruleDO);
        ruleBO.setId(ruleDO.getId());
    }

    @Override
    public PageInfo<ConfigRuleBO> queryByPage(RuleQuery query, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        ConfigRuleDO configRulePackageDO = convertQoToDo(query);
        Page<ConfigRuleDO> rulePackageDOPage = (Page<ConfigRuleDO>) ruleMapper.query(configRulePackageDO);
        return PageUtils.convertPage(rulePackageDOPage, ConfigRuleBO::convertFromDo);
    }

    @Override
    public void modify(ConfigRuleBO ruleBO) {
        ConfigRuleDO ruleDO = ruleBO.convertToDo();
        ruleMapper.update(ruleDO);
    }

    private ConfigRuleDO convertQoToDo(RuleQuery rulePackageQuery) {
        ConfigRuleDO ruleDO = new ConfigRuleDO();
        if (rulePackageQuery != null) {
            BeanUtils.copyProperties(rulePackageQuery, ruleDO);
        }
        return ruleDO;
    }
}
