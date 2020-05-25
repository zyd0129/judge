package com.ps.judge.provider.listener;

import com.ps.judge.dao.entity.RiskDO;
import com.ps.judge.dao.entity.RuleDO;
import com.ps.judge.dao.entity.TriggeredRuleDO;
import com.ps.judge.dao.mapper.RuleMapper;
import com.ps.judge.dao.mapper.TriggeredRuleMapper;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.rule.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Drools 议程监听器
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/15
 */
@Component
public class AgendaEventListenerImpl implements AgendaEventListener {
    @Autowired
    RuleMapper ruleMapper;
    @Autowired
    TriggeredRuleMapper triggeredRuleMapper;

    private ThreadLocal<RiskDO> riskThreadLocal = new ThreadLocal();
    public void setRiskThreadLocal(RiskDO risk) {
        this.riskThreadLocal.set(risk);
    }

    @Override
    public void matchCreated(MatchCreatedEvent matchCreatedEvent) {
        //System.out.println("matchCreated " + matchCreatedEvent);
    }

    @Override
    public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {
        //System.out.println("matchCancelled" + matchCancelledEvent);
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {
       /* System.out.println("beforeMatchFired " + beforeMatchFiredEvent);

        System.out.println("beforeMatchFired  KieRuntime" + beforeMatchFiredEvent.getKieRuntime());
        System.out.println("beforeMatchFired  KieRuntime" + beforeMatchFiredEvent.getMatch());

        Match match = beforeMatchFiredEvent.getMatch();

        Rule rule = match.getRule();

        System.out.println("afterMatchFired  rule " + rule);*/
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
        Match match = afterMatchFiredEvent.getMatch();
        RuleImpl ruleImpl =(RuleImpl) match.getRule();
        System.out.println("RuleImpl " + ruleImpl);
        String ruleCode = ruleImpl.getName();

        RuleDO ruleDO = this.ruleMapper.getRuleByCode(ruleCode);
        TriggeredRuleDO triggeredRule = new TriggeredRuleDO();
        triggeredRule.setTenantId(ruleDO.getTenantId());
        //triggeredRule.setApplyId();
        //triggeredRule.setFlow();
        triggeredRule.setRulePackageCode(ruleDO.getPackageCode());
        triggeredRule.setRuleCode(ruleDO.getCode());
        //triggeredRule.setParam();
        triggeredRule.setCreateTime(LocalDateTime.now());
        System.out.println("TriggeredRuleDO " + triggeredRule);
        this.triggeredRuleMapper.insert(triggeredRule);

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
        //System.out.println("agendaGroupPopped" + agendaGroupPoppedEvent);
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent agendaGroupPushedEvent) {
        //System.out.println("agendaGroupPushed" + agendaGroupPushedEvent);
    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {
        //System.out.println("beforeRuleFlowGroupActivated" + ruleFlowGroupActivatedEvent);
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {
        //System.out.println("afterRuleFlowGroupActivated" + ruleFlowGroupActivatedEvent);
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {
        //System.out.println("beforeRuleFlowGroupDeactivated" + ruleFlowGroupDeactivatedEvent);
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {
        //System.out.println("afterRuleFlowGroupDeactivated" + ruleFlowGroupDeactivatedEvent);
    }
}
