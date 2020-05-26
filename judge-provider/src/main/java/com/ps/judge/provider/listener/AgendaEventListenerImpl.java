package com.ps.judge.provider.listener;

import org.kie.api.event.rule.*;

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
        /*Match match = afterMatchFiredEvent.getMatch();
        RuleImpl ruleImpl =(RuleImpl) match.getRule();
        System.out.println("RuleImpl " + ruleImpl);
        String ruleCode = ruleImpl.getName();

        RuleDO ruleDO = this.ruleMapper.getRuleByCode(ruleCode);
        AuditTaskTriggeredRuleDO triggeredRule = new AuditTaskTriggeredRuleDO();
        triggeredRule.setTenantId(ruleDO.getTenantId());
        //triggeredRule.setApplyId();
        //triggeredRule.setFlow();
        triggeredRule.setRulePackageCode(ruleDO.getPackageCode());
        triggeredRule.setRuleCode(ruleDO.getCode());
        //triggeredRule.setParam();
        triggeredRule.setCreateTime(LocalDateTime.now());
        System.out.println("AuditTaskTriggeredRuleDO " + triggeredRule);
        this.triggeredRuleMapper.insert(triggeredRule);*/

        /*System.out.println("afterMatchFired " + afterMatchFiredEvent);
        KieRuntime kieRuntime = afterMatchFiredEvent.getKieRuntime();
        System.out.println("afterMatchFired  KieRuntime " + kieRuntime);

        Match match = afterMatchFiredEvent.getMatch();
        System.out.println("afterMatchFired  Match " + match);
        System.out.println("afterMatchFired  Match " + match.getDeclarationIds());
        System.out.println("afterMatchFired  Match " + match.getObjects());
        System.out.println("afterMatchFired  Match " + match.getFactHandles());

        //System.out.println("afterMatchFired  Match " + match.getDeclarationValue("when"));
        System.out.println("afterMatchFired  Match " + match.getDeclarationValue("$person"));
        //System.out.println("afterMatchFired  Match " + match.getDeclarationValue("when"));

        RuleImpl rule =(RuleImpl) match.getRule();

        System.out.println("afterMatchFired  rule " + rule);
        System.out.println("afterMatchFired  rule id " + rule.getId());
        System.out.println("afterMatchFired  rule name " + rule.getName());
        System.out.println("afterMatchFired  rule activationGroup " + rule.getActivationGroup());
        System.out.println("afterMatchFired  rule agendaGroup " + rule.getAgendaGroup());

        System.out.println("afterMatchFired  rule getFullyQualifiedName " + rule.getFullyQualifiedName());
        System.out.println("afterMatchFired  rule getDependingQueries " + rule.getDependingQueries());
        System.out.println("afterMatchFired  rule getDependingQueries " + rule.getNamedConsequences());
        System.out.println("afterMatchFired  rule Declarations " + rule.getDeclarations());
        System.out.println("afterMatchFired  rule Namespace " + rule.getNamespace());
        System.out.println("afterMatchFired  rule metaData " + rule.getMetaData());
        System.out.println("afterMatchFired  rule metaData size " + rule.getMetaData().size());
        System.out.println("afterMatchFired  rule KnowledgeType " +  rule.getKnowledgeType());

        Map<String, Object> map = rule.getMetaData();
        for(Map.Entry<String, Object>  entry: map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }*/

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
