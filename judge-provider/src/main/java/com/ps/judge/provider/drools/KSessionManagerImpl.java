package com.ps.judge.provider.drools;

import com.ps.judge.provider.exception.BizException;
import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.models.ConfigPackageBO;
import com.ps.judge.provider.service.ConfigFlowService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class KSessionManagerImpl implements KSessionManager {
    @Autowired
    private ConfigFlowService configFlowService;

    private KieServices kieServices=KieServices.Factory.get();;

    private Map<String, KieContainer> kieContainerHashMap = new ConcurrentHashMap<>();

    @Override
    public KieSession getKieSession(String flowCode) {
        KieContainer kieContainer = kieContainerHashMap.get(flowCode);
        if (kieContainer == null) {
            return null;
        }
        return kieContainer.newKieSession();
    }

    @Override
    public StatelessKieSession getStatelessKieSession(String flowCode) {
        KieContainer kieContainer = kieContainerHashMap.get(flowCode);
        if (kieContainer == null) {
            return null;
        }
        return kieContainer.newStatelessKieSession();
    }

    @Override
    public void addContainer(ConfigFlowBO configFlow) {
        ReleaseId releaseId = kieServices.newReleaseId(configFlow.getCurPackageGroup(), configFlow.getCurPackageArtifact(), configFlow.getCurPackageVersion());
        try {
            KieContainer kieContainer = kieServices.newKieContainer(releaseId);
            kieContainerHashMap.put(configFlow.getFlowCode(), kieContainer);
        } catch (Exception e) {
            throw new BizException(BizException.BizExceptionEnum.CONTAINER_START_FAILURE);
        }
//        KieContainerSessionsPool pool = kieContainer.newKieSessionsPool(10);
    }

    @Override
    public void removeContainer(ConfigFlowBO configFlow) {
        KieContainer kieContainer = kieContainerHashMap.remove(configFlow.getFlowCode());
//
//        if (kieContainer != null) {
//            kieContainer.dispose();
//        }
    }

    @Override
    public boolean existedContainer(ConfigFlowBO configFlow) {

        return kieContainerHashMap.containsKey(configFlow.getFlowCode());
    }
}
