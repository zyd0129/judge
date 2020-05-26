package com.ps.judge.provider.drools;

import com.ps.judge.provider.models.ConfigFlowBO;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

public interface KSessionManager {

    KieSession getKieSession(String flowCode);

    StatelessKieSession getStatelessKieSession(String flowCode);

    void addKieModule(ConfigFlowBO configFlow);

    void removeKieModule(ConfigFlowBO configFlow);
}
