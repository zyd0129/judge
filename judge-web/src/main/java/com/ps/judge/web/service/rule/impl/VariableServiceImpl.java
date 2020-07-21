package com.ps.judge.web.service.rule.impl;

import com.ps.judge.dao.entity.ConfigRuleVariableDO;
import com.ps.judge.dao.mapper.ConfigRuleVariableMapper;
import com.ps.judge.web.pojo.bo.ConfigRuleVariableBO;
import com.ps.judge.web.pojo.query.VariableQuery;
import com.ps.judge.web.service.rule.VariableService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariableServiceImpl implements VariableService {

    @Autowired
    ConfigRuleVariableMapper variableMapper;

    @Override
    public List<ConfigRuleVariableBO> query(VariableQuery variableQuery) {
        return variableMapper.query(convertQoToDo(variableQuery)).stream().map(ConfigRuleVariableBO::convertFromDo).collect(Collectors.toList());
    }


    private ConfigRuleVariableDO convertQoToDo(VariableQuery variableQuery) {
        ConfigRuleVariableDO ruleDO = new ConfigRuleVariableDO();
        if (variableQuery != null) {
            BeanUtils.copyProperties(variableQuery, ruleDO);
        }
        return ruleDO;
    }
}
