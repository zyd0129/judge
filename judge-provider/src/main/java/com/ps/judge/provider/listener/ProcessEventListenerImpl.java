package com.ps.judge.provider.listener;

import org.kie.api.event.process.*;

/**
 * Drools 业务流程（JBPM）监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
//@Component
public class ProcessEventListenerImpl implements ProcessEventListener {
    @Override
    public void beforeProcessStarted(ProcessStartedEvent processStartedEvent) {
        //System.out.println("beforeProcessStarted" + processStartedEvent);
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent processStartedEvent) {
        //System.out.println("afterProcessStarted" + processStartedEvent);
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent processCompletedEvent) {
        //System.out.println("beforeProcessCompleted" + processCompletedEvent);
    }

    public void afterProcessCompleted(ProcessCompletedEvent processCompletedEvent) {
        //System.out.println("afterProcessCompleted" + processCompletedEvent);
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {
        //System.out.println("beforeNodeTriggered" + processNodeTriggeredEvent);
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {
        //System.out.println("afterNodeTriggered" + processNodeTriggeredEvent);
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {
        //System.out.println("beforeNodeLeft" + processNodeLeftEvent);
    }

    public void afterNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {
        //System.out.println("afterNodeLeft" + processNodeLeftEvent);
    }

    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {
        //System.out.println("beforeVariableChanged" + processVariableChangedEvent);
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {
        //System.out.println("afterVariableChanged" + processVariableChangedEvent);
    }

    @Override
    public void beforeSLAViolated(SLAViolatedEvent event) {
        //System.out.println("beforeSLAViolated" + event);
    }

    @Override
    public void afterSLAViolated(SLAViolatedEvent event) {
        //System.out.println("afterSLAViolated" + event);
    }
}
