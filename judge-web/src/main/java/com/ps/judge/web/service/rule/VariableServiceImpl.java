package com.ps.judge.web.service.rule;

import com.ps.common.query.VariableQuery;
import com.ps.judge.dao.entity.ConfigRuleVariableDO;
import com.ps.judge.dao.mapper.ConfigRuleVariableMapper;
import com.ps.judge.web.models.ConfigRuleVariableBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VariableServiceImpl implements VariableService {
    @Autowired
    ConfigRuleVariableMapper variableMapper;
    @Override
    public List<ConfigRuleVariableBO> all() {
        List<ConfigRuleVariableDO>  variableDOList = variableMapper.all();
        return convertToBOS(variableDOList);
    }

    @Override
    public List<ConfigRuleVariableBO> query(VariableQuery variableQuery) {
        ConfigRuleVariableDO queryDO = new ConfigRuleVariableDO();
        if (variableQuery != null) {
            BeanUtils.copyProperties(variableQuery,queryDO);
        }
        List<ConfigRuleVariableDO> variableDOList = variableMapper.query(queryDO);
        return convertToBOS(variableDOList);
    }


    private List<ConfigRuleVariableBO> convertToBOS(List<ConfigRuleVariableDO> variableDOList) {
        if (variableDOList==null) return null;

        return variableDOList.stream().map(this::convertToBO).collect(Collectors.toList());
    }

    private ConfigRuleVariableBO convertToBO(ConfigRuleVariableDO variableDO) {
        ConfigRuleVariableBO variableBO = new ConfigRuleVariableBO();
        BeanUtils.copyProperties(variableDO, variableBO);
        return variableBO;
    }
}
