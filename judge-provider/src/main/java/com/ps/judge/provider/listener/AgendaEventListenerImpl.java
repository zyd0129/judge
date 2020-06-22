package com.ps.judge.provider.listener;

import org.drools.core.definitions.rule.impl.RuleImpl;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.rule.Match;

/**
 * Drools 议程监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
public class AgendaEventListenerImpl implements AgendaEventListener {
    @Override
    public void matchCreated(MatchCreatedEvent matchCreatedEvent) {
    }

    @Override
    public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
        Match match = afterMatchFiredEvent.getMatch();
        RuleImpl ruleImpl = (RuleImpl) match.getRule();
        String ruleCode = ruleImpl.getName();
        System.err.println("afterMatchFired rule : " + ruleCode);
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent agendaGroupPoppedEvent) {
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent agendaGroupPushedEvent) {
    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {
    }
}
