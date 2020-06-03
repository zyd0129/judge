package com.ps.judge.provider.drools;

import com.ps.judge.dao.entity.ConfigFlowDO;
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
    public void init() {
        log.info("start initiating kieContainerHashMap ");
        long startTime = System.currentTimeMillis();
        List<ConfigFlowDO> configFlowList = this.configFlowService.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            try {
                this.kSessionManager.addContainer(configFlow);
                log.error("container for {} start success", configFlow.getFlowCode());
            } catch (Exception e) {
                log.error("container for {} start failure", configFlow.getFlowCode());
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("end initiating kieContainerHashMap, time cost: {}", endTime - startTime);
    }
}
