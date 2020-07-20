package com.ps.judge.provider.service;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * flow管理服务
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/20
 */
public interface FlowService {
    ConfigFlowDO getByFlowCode(String flowCode);

    List<ConfigFlowDO> getAllEnable();

    boolean initFlow();

    boolean loadFlow(ConfigFlowDO configFlow);

    boolean removeFlow(ConfigFlowDO configFlow);

    KieSession getKieSession(String flowCode);
}
