package com.ts.judge.provider.flow.rule.executor;

import com.ts.clerk.common.exception.BizException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;
import java.util.Map;

/**
 * 规则执行接口
 *
 * @author ：zhangqian9044.
 * @date ：2020/7/14
 */
public interface ExpressionExecutor {
    boolean executor(String expr, Map<String, Object> params) throws BizException;
}
