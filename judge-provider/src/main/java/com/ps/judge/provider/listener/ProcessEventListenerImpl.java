package com.ps.judge.provider.listener;

import org.kie.api.event.process.*;

/**
 * Drools 业务流程（JBPM）监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
public class ProcessEventListenerImpl implements ProcessEventListener {
    @Override
    public void beforeProcessStarted(ProcessStartedEvent processStartedEvent) {
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent processStartedEvent) {
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent processCompletedEvent) {
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent processCompletedEvent) {
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {
    }

    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {
    }

    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {
    }

    @Override
    public void beforeSLAViolated(SLAViolatedEvent event) {
    }

    @Override
    public void afterSLAViolated(SLAViolatedEvent event) {
    }
}
