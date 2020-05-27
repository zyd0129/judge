package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.provider.exception.BizException;
import com.ps.judge.provider.models.ConfigFlowBO;

import java.util.List;

public interface ConfigFlowService {
    List<ConfigFlowBO> getAll();
    List<ConfigFlowBO> query(int pageNo, int size);

    ConfigFlowBO getByFlowCode(String flowCode);
    ConfigFlowBO getById(int id);

    void insert(ConfigFlowBO configFlowBO);

    void updateStatus(ConfigFlowBO configFlowBO) throws BizException;
    void changePackage(ConfigFlowBO configFlowBO);
}
