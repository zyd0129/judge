package com.ps.judge.provider.rule.load;

import org.drools.core.impl.InternalKnowledgeBase;

/**
 * 功能描述
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/16
 */
public interface Loan {
    InternalKnowledgeBase load(String ruleStr);
}