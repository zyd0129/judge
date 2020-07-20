package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.runtime.KieSession;

import java.util.List;

public interface ConfigFlowService {
    ConfigFlowDO getByFlowCode(String flowCode);

    List<ConfigFlowDO> getAllEnable();

    boolean initFlow();

    boolean loadFlow(ConfigFlowDO configFlow);

    boolean removeFlow(ConfigFlowDO configFlow);

    KieSession getKieSession(String flowCode);
}
