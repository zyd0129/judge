package com.ps.judge.api.entity;

import lombok.Data;

/**
 * 规则命中记录
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/18
 */
@Data
public class TriggeredRuleVO {
    private String ruleCode;
    private String ruleName;
    //private String expression;
    //private String condition;
    //private String param;
    //private String triggeredResult;
}
