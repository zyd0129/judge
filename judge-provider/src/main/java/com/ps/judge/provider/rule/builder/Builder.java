package com.ps.judge.provider.rule.builder;

import com.ps.judge.provider.rule.model.RuleVO;

import java.util.List;

/**
 * 规则创建接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
public interface Builder {

    String build(RuleVO rule);

    String buildAll(List<RuleVO> ruleList);

}
