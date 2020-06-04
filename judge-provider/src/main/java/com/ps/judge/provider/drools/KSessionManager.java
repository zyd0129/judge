package com.ps.judge.provider.drools;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

public interface KSessionManager {

    KieSession getKieSession(String flowCode);

    StatelessKieSession getStatelessKieSession(String flowCode);

    boolean addContainer(ConfigFlowDO configFlow);

    boolean removeContainer(ConfigFlowDO configFlow);

    boolean existedContainer(ConfigFlowDO configFlow);
}
