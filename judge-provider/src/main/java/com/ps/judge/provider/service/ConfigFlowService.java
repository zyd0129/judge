package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;

import java.util.List;

public interface ConfigFlowService {
    ConfigFlowDO getByFlowCode(String flowCode);

    List<ConfigFlowDO> getAllEnable();

    boolean loadFlow(ConfigFlowDO configFlow);

    boolean unLoadFlow(ConfigFlowDO configFlow);
}
