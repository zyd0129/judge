package com.ps.judge.web.service.impl;

import com.ps.judge.dao.entity.AuditTaskTriggeredRuleDO;
import com.ps.judge.dao.mapper.AuditTaskTriggeredRuleMapper;
import com.ps.judge.web.models.AuditTaskTriggeredRuleBO;
import com.ps.judge.web.service.AuditTaskTriggeredRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditTaskTriggeredRuleServiceImpl implements AuditTaskTriggeredRuleService {
    @Autowired
    AuditTaskTriggeredRuleMapper triggeredRuleMapper;

    @Override
    public List<AuditTaskTriggeredRuleBO> queryByTaskId(int id) {
        return convertToBOList(triggeredRuleMapper.queryByTaskId(id));
    }

    private List<AuditTaskTriggeredRuleBO> convertToBOList(List<AuditTaskTriggeredRuleDO> auditTaskTriggeredRuleDOList) {
        if (auditTaskTriggeredRuleDOList == null) {
            return null;
        }
        return auditTaskTriggeredRuleDOList.stream().map(o -> convertToBO(o)).collect(Collectors.toList());
    }

    private AuditTaskTriggeredRuleBO convertToBO(AuditTaskTriggeredRuleDO auditTaskParamDO) {
        if (auditTaskParamDO == null) {
            return null;
        }
        AuditTaskTriggeredRuleBO auditTaskParamBO = new AuditTaskTriggeredRuleBO();
        BeanUtils.copyProperties(auditTaskParamDO, auditTaskParamBO);
        return auditTaskParamBO;
    }
}
