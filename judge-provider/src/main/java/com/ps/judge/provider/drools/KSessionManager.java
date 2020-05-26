package com.ps.judge.provider.drools;

import com.ps.judge.provider.models.ConfigFlowBO;
import org.kie.api.runtime.KieSession;

public interface KSessionManager {
    KieSession getFlowSession(String flowName);

    void addKieModule(ConfigFlowBO configFlow);
    void removeKieModule(ConfigFlowBO configFlow);
}
