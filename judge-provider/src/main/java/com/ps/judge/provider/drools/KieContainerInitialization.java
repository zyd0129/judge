package com.ps.judge.provider.drools;

import com.ps.judge.provider.models.ConfigFlowBO;
import com.ps.judge.provider.service.ConfigFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class KieContainerInitialization {
    @Autowired
    ConfigFlowService configFlowService;

    @Autowired
    KSessionManager kSessionManager;

    @PostConstruct
    public void init(){
        log.info("start initiating kieContainerHashMap ");
        long startTime = System.currentTimeMillis();
        List<ConfigFlowBO> configFlowList = configFlowService.getAll();
        for (ConfigFlowBO configFlow : configFlowList) {
            if (ConfigFlowBO.Status.STARTED.equals(configFlow.getStatus())) {
                try {
                    kSessionManager.addContainer(configFlow);
                } catch (Exception e) {
                    log.error("container for {}-{} start failure", configFlow.getFlowCode(), configFlow.getReleaseId());
                    configFlow.setStatus(ConfigFlowBO.Status.STOPPED);
                    configFlow.setOperator("system");
                    configFlowService.updateStatus(configFlow);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("end initiating kieContainerHashMap, time cost: {}", endTime - startTime);
    }
}
