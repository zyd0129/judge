package com.ps.judge.web.service.rule.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ps.judge.dao.entity.ConfigRuleConditionDO;
import com.ps.judge.dao.mapper.ConfigRuleConditionMapper;
import com.ps.judge.web.pojo.bo.ConfigRuleConditionBO;
import com.ps.judge.web.pojo.query.ConditionQuery;
import com.ps.judge.web.service.rule.ConditionService;
import com.ps.judge.web.util.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    ConfigRuleConditionMapper conditionMapper;

    @Override
    public void create(ConfigRuleConditionBO ruleBO) {
        ConfigRuleConditionDO conditionDO = ruleBO.convertToDo();
        conditionMapper.insert(conditionDO);
        ruleBO.setId(conditionDO.getId());
    }

    @Override
    public PageInfo<ConfigRuleConditionBO> queryByPage(ConditionQuery query, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        ConfigRuleConditionDO conditionDO = convertQoToDo(query);
        Page<ConfigRuleConditionDO> conditionDOPage = (Page<ConfigRuleConditionDO>) conditionMapper.query(conditionDO);
        return PageUtils.convertPage(conditionDOPage, ConfigRuleConditionBO::convertFromDo);
    }

    @Override
    public void modify(ConfigRuleConditionBO conditionBO) {
        ConfigRuleConditionDO conditionDO = conditionBO.convertToDo();
        conditionMapper.update(conditionDO);
    }

    private ConfigRuleConditionDO convertQoToDo(ConditionQuery conditionQuery) {
        ConfigRuleConditionDO ruleDO = new ConfigRuleConditionDO();
        if (conditionQuery != null) {
            BeanUtils.copyProperties(conditionQuery, ruleDO);
        }
        return ruleDO;
    }
}
