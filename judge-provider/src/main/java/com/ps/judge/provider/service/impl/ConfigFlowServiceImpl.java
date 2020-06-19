package com.ps.judge.provider.service.impl;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.dao.mapper.ConfigFlowMapper;
import com.ps.judge.provider.drools.KSessionManager;
import com.ps.judge.provider.service.ConfigFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigFlowServiceImpl implements ConfigFlowService {
    @Autowired
    ConfigFlowMapper configFlowMapper;
    @Autowired
    KSessionManager kSessionManager;

    @Override
    public ConfigFlowDO getByFlowCode(String flowCode) {
        return this.configFlowMapper.getByFlowCode(flowCode);
    }

    @Override
    public List<ConfigFlowDO> getAllEnable() {
        return  this.configFlowMapper.getAllEnable();
    }

    @Override
    public boolean loadFlow(ConfigFlowDO configFlow) {
        return this.kSessionManager.addContainer(configFlow);
    }

    @Override
    public boolean unLoadFlow(ConfigFlowDO configFlow) {
        return this.kSessionManager.removeContainer(configFlow);
    }
}
