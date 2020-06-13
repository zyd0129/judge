package com.ps.judge.web.service.impl;

import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.dao.entity.AuditTaskParamDO;
import com.ps.judge.dao.mapper.AuditTaskParamMapper;
import com.ps.judge.web.models.AuditTaskBO;
import com.ps.judge.web.models.AuditTaskParamBO;
import com.ps.judge.web.service.AuditTaskParamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditTaskParamServiceImple implements AuditTaskParamService {
    @Autowired
    AuditTaskParamMapper paramMapper;

    @Override
    public AuditTaskParamBO getById(int id) {
        return convertToBO(paramMapper.getAuditTaskParam(id));
    }

    private AuditTaskParamBO convertToBO(AuditTaskParamDO auditTaskParamDO) {
        if (auditTaskParamDO == null) {
            return null;
        }
        AuditTaskParamBO auditTaskParamBO = new AuditTaskParamBO();
        BeanUtils.copyProperties(auditTaskParamDO, auditTaskParamBO);
        return auditTaskParamBO;
    }
}
