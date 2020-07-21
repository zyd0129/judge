package com.ps.judge.provider.drools;

import com.ps.judge.dao.entity.ConfigFlowDO;
import com.ps.judge.provider.service.FlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KieContainerInitialization {
    @Autowired
    FlowService flowService;

    @Autowired
    KSessionManager kSessionManager;

    //@PostConstruct
    public void init() {
        log.info("start initiating kieContainerHashMap ");
        long startTime = System.currentTimeMillis();
        List<ConfigFlowDO> configFlowList = this.flowService.getAllEnable();
        for (ConfigFlowDO configFlow : configFlowList) {
            if (configFlow.getLoadMethod() == 0) {
                continue;
            }
            try {
                this.kSessionManager.addContainer(configFlow);
                log.info("container for {} start success", configFlow.getFlowCode());
            } catch (Exception e) {
                log.error("container for {} start failure", configFlow.getFlowCode());
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("end initiating kieContainerHashMap, time cost: {}", endTime - startTime);
    }
}
