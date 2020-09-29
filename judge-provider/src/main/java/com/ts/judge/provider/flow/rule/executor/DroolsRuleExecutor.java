package com.ts.judge.provider.flow.rule.executor;

import org.drools.core.command.runtime.rule.AgendaGroupSetFocusCommand;

import org.kie.api.command.Command;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/22
 */
public class DroolsRuleExecutor implements RuleExecutor {
    @Override
    public void executor(KieSession kieSession, List factList, String ruleFlowGroup) {
        for (Object fact : factList) {
            kieSession.insert(fact);
        }
        kieSession.getAgenda().getAgendaGroup(ruleFlowGroup).setFocus();
        kieSession.fireAllRules();
        //移除fact对象不影响kieSession下次调用
        for (Object fact : factList) {
            kieSession.delete(kieSession.getFactHandle(fact));
        }
        kieSession.dispose();
    }

    @Override
    public void executor(StatelessKieSession statelessKieSession, List factList, String ruleFlowGroup) {
        List<Command> commandList = new ArrayList();
        for (Object fact : factList) {
            commandList.add(CommandFactory.newInsert(fact));
        }
        commandList.add(new AgendaGroupSetFocusCommand(ruleFlowGroup));
        statelessKieSession.execute(CommandFactory.newBatchExecution(commandList));
    }
}
