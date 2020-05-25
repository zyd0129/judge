package com.ps.judge.provider.listener;

import com.ps.judge.dao.entity.RiskDO;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.springframework.stereotype.Component;

/**
 * Drools 规则监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
//@Component
public class RuleRuntimeEventListenerImpl implements RuleRuntimeEventListener {
    private ThreadLocal<RiskDO> riskThreadLocal = new ThreadLocal();
    public void setRiskThreadLocal(RiskDO risk) {
        this.riskThreadLocal.set(risk);
    }

    @Override
    public void objectInserted(ObjectInsertedEvent objectInsertedEvent) {
        //System.out.println("objectInserted" + objectInsertedEvent);
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent objectUpdatedEvent) {
        //System.out.println("objectUpdated" + objectUpdatedEvent);
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent objectDeletedEvent) {
        //System.out.println("objectDeleted" + objectDeletedEvent);
    }
}
