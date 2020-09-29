package com.ts.judge.provider.flow.rule.context;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;

/**
 * 规则加载接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public interface RuleContext {
    KieSession getKieSession(String flowCode);

    StatelessKieSession getStatelessKieSession(String flowCode);

    boolean add(String flowCode, String ruleStr);

    boolean add(String flowCode, List<String> ruleStrList);

    boolean remove(String flowCode);

    boolean existed(String flowCode);
}