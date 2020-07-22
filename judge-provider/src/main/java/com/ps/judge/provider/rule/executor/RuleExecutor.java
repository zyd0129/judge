package com.ps.judge.provider.rule.executor;


import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * 规则执行接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
public interface RuleExecutor {
    void executor(KieSession kieSession, List paramList, String ruleFlowGroup);
}
