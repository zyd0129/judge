package com.ps.judge.provider.rule.executor;

import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/22
 */
public class DroolsRuleExecutor implements RuleExecutor {
    @Override
    public void executor(KieSession kieSession, List paramList, String ruleFlowGroup) {
        for (Object param : paramList) {
            kieSession.insert(param);
        }
        //kieSession.getAgenda().getAgendaGroup(ruleFlowGroup).setFocus();
        kieSession.fireAllRules();
    }
}
