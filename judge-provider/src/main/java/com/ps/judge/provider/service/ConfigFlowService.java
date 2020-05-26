package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.provider.models.ConfigFlowBO;

import java.util.List;

public interface ConfigFlowService {
    List<ConfigFlowBO> getAll();

    ConfigFlowBO getByFlowCode(String flowCode);

    void insert(ConfigFlowBO configFlowBO);

    void updateStatus(ConfigFlowBO configFlowBO);

    void changePackage(ConfigFlowBO configFlowBO);
}
