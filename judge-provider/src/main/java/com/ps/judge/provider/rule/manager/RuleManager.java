package com.ps.judge.provider.rule.manager;

import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * 规则加载接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public interface RuleManager {

    KieSession getKieSession(String flowCode);

    boolean add(String flowCode, String ruleStr);

    boolean add(String flowCode, List<String> ruleStrList);

    boolean remove(String flowCode);

    boolean existed(String flowCode);
}