package com.ps.judge.provider.initialization;

import com.ps.judge.provider.drools.KieContainerInitialization;
import com.ps.judge.provider.service.ConfigFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 服务初始化时，将数据加载到各个Context中
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
@Component
@Slf4j
public class JudgeProviderInitialization {
    @Autowired
    KieContainerInitialization kieContainerInitialization;
    @Autowired
    ConfigFlowService configFlowService;

    @PostConstruct
    public void init() {
        log.info("start provider initiating ");
        long startTime = System.currentTimeMillis();
        this.kieContainerInitialization.init();
        this.configFlowService.initFlow();
        long endTime = System.currentTimeMillis();
        log.info("end provider initiating , time cost: {}", endTime - startTime);
    }



}
