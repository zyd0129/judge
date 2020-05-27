package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.exception.BizException;
import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.service.ConfigFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigFlowServiceImpl implements ConfigFlowService {
    @Autowired
    ConfigFlowMapper flowMapper;
    @Autowired
    private KSessionManager kSessionManager;

    @Override
    public List<ConfigFlowBO> getAll() {
        List<ConfigFlowDO> configFlowDOList = flowMapper.getAll();
        if (configFlowDOList == null) {
            return null;
        }
        List<ConfigFlowBO> configFlowBOList = new ArrayList<>();
        for (ConfigFlowDO configFlowDO : configFlowDOList) {
            ConfigFlowBO configFlowBO = convertToBO(configFlowDO);
            configFlowBOList.add(configFlowBO);
        }
        return configFlowBOList;
    }

    @Override
    public ConfigFlowBO getByFlowCode(String flowCode) {
        ConfigFlowDO configFlowDO = flowMapper.getByFlowCode(flowCode);
        if (configFlowDO == null) {
            return null;
        }
        return convertToBO(configFlowDO);
    }

    @Override
    public ConfigFlowBO getById(int id) {
        ConfigFlowDO configFlowDO = flowMapper.getById(id);
        if (configFlowDO == null) {
            return null;
        }
        return convertToBO(configFlowDO);
    }

    @Override
    public void insert(ConfigFlowBO configFlowBO) {
        ConfigFlowDO configFlowDO = convertToDO(configFlowBO);
        configFlowDO.setGmtCreated(LocalDateTime.now());
        flowMapper.insert(configFlowDO);
        configFlowBO.setId(configFlowDO.getId());
    }

    @Override
//    @Transactional
    public void updateStatus(ConfigFlowBO configFlowBO) throws BizException {
        ConfigFlowDO configFlowDO = convertToDO(configFlowBO);
        configFlowDO.setGmtModified(LocalDateTime.now());
        flowMapper.updateStatus(configFlowDO);

        if (ConfigFlowBO.Status.STARTED.equals(configFlowBO.getStatus())) {
            configFlowBO = getById(configFlowDO.getId());
            kSessionManager.addContainer(configFlowBO);
        } else {
            kSessionManager.removeContainer(configFlowBO);
        }
    }

    @Override
    public void changePackage(ConfigFlowBO configFlowBO) {
        ConfigFlowDO configFlowDO = convertToDO(configFlowBO);
        configFlowDO.setGmtModified(LocalDateTime.now());
        flowMapper.changePackage(configFlowDO);
    }

    private ConfigFlowBO convertToBO(ConfigFlowDO configFlowDO) {
        if (configFlowDO == null) {
            return null;
        }
        ConfigFlowBO configFlowBO = new ConfigFlowBO();
        BeanUtils.copyProperties(configFlowDO, configFlowBO);
        configFlowBO.setStatus(ConfigFlowBO.Status.valueOf(configFlowDO.getStatus()));
        return configFlowBO;
    }

    private ConfigFlowDO convertToDO(ConfigFlowBO configFlowBO) {
        if (configFlowBO == null) {
            return null;
        }
        ConfigFlowDO configFlowDO = new ConfigFlowDO();
        BeanUtils.copyProperties(configFlowBO, configFlowDO);
        configFlowDO.setStatus(configFlowBO.getStatus().getValue());
        return configFlowDO;
    }
}
