package com.ps.judge.provider.drools;

import com.ps.judge.dao.entity.ConfigFlowDO;
import lombok.extern.slf4j.Slf4j;

import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class KSessionManagerImpl implements KSessionManager {
    private KieServices kieServices = KieServices.Factory.get();

    private Map<String, KieContainer> kieContainerHashMap = new ConcurrentHashMap<>();

    @Override
    public KieSession getKieSession(String flowCode) {
        KieContainer kieContainer = this.kieContainerHashMap.get(flowCode);
        if (kieContainer == null) {
            return null;
        }
        return kieContainer.newKieSession();
    }

    @Override
    public StatelessKieSession getStatelessKieSession(String flowCode) {
        KieContainer kieContainer = this.kieContainerHashMap.get(flowCode);
        if (kieContainer == null) {
            return null;
        }
        return kieContainer.newStatelessKieSession();
    }

    @Override
    public boolean addContainer(ConfigFlowDO configFlow) {
        //Kie资料库
        KieRepository kieRepository = this.kieServices.getRepository();
        UrlResource urlResource = (UrlResource) this.kieServices.getResources().newUrlResource(configFlow.getPackageUrl());
        InputStream is = null;
        try {
            is = urlResource.getInputStream();
        } catch (IOException e) {
            if (Objects.nonNull(is)) {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            log.error("addContainer failure, message : {}", e.getMessage());
            return false;
        }
        //获取资源
        Resource resource = this.kieServices.getResources().newInputStreamResource(is);

        //获取加载资源获取KieModule
        KieModule kieModule = kieRepository.addKieModule(resource);
        //通过kieModule的ReleaseId获取kieContainer
        KieContainer kieContainer = this.kieServices.newKieContainer(kieModule.getReleaseId());
        this.kieContainerHashMap.put(configFlow.getFlowCode(), kieContainer);
        return true;
    }

    @Override
    public boolean removeContainer(ConfigFlowDO configFlow) {
        KieContainer kieContainer = this.kieContainerHashMap.remove(configFlow.getFlowCode());
        if (Objects.nonNull(kieContainer)) {
            kieContainer.dispose();
        }
        return true;
    }

    @Override
    public boolean existedContainer(ConfigFlowDO configFlow) {
        return this.kieContainerHashMap.containsKey(configFlow.getFlowCode());
    }
}
