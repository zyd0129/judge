package com.ts.judge.provider.flow.rule.executor;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;

/**
 * 规则执行接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
public interface RuleExecutor {
    void executor(KieSession kieSession, List factList, String ruleFlowGroup);

    void executor(StatelessKieSession statelessKieSession, List factList, String ruleFlowGroup);
}
