package com.ps.judge.provider.listener;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

/**
 * Drools 规则监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
public class RuleRuntimeEventListenerImpl implements RuleRuntimeEventListener {
    @Override
    public void objectInserted(ObjectInsertedEvent objectInsertedEvent) {
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent objectUpdatedEvent) {
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent objectDeletedEvent) {
    }
}
