package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.service.ConfigFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigFlowServiceImpl implements ConfigFlowService {
    @Autowired
    ConfigFlowMapper flowMapper;
    @Override
    public List<ConfigFlowBO> getAll() {
        List<ConfigFlowDO> configFlowDOList = flowMapper.getAll();
        if (configFlowDOList == null) {
            return null;
        }
        List<ConfigFlowBO> configFlowBOList = new ArrayList<>();
        for (ConfigFlowDO configFlowDO : configFlowDOList) {
            ConfigFlowBO configFlowBO = new ConfigFlowBO();
            BeanUtils.copyProperties(configFlowDO,configFlowBO);
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
        ConfigFlowBO configFlowBO = new ConfigFlowBO();
        BeanUtils.copyProperties(configFlowDO,configFlowBO);
        return configFlowBO;
    }

    @Override
    public void insert(ConfigFlowBO configFlowBO) {
        ConfigFlowDO configFlowDO = new ConfigFlowDO();
        BeanUtils.copyProperties(configFlowBO, configFlowDO);
        configFlowDO.setGmtCreated(LocalDateTime.now());
        flowMapper.insert(configFlowDO);
        configFlowBO.setId(configFlowDO.getId());
    }

    @Override
    public void updateStatus(ConfigFlowBO configFlowBO) {
        ConfigFlowDO configFlowDO = new ConfigFlowDO();
        BeanUtils.copyProperties(configFlowBO, configFlowDO);
        configFlowDO.setGmtModified(LocalDateTime.now());
        flowMapper.updateStatus(configFlowDO);
    }

    @Override
    public void changePackage(ConfigFlowBO configFlowBO) {
        ConfigFlowDO configFlowDO = new ConfigFlowDO();
        BeanUtils.copyProperties(configFlowBO, configFlowDO);
        configFlowDO.setGmtModified(LocalDateTime.now());
        flowMapper.changePackage(configFlowDO);
    }
}
