package com.ts.judge.provider.flow.rule.executor;

import com.ts.clerk.common.exception.BizException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GroovyExpressionExecutor implements ExpressionExecutor {
    @Override
    public boolean executor(String expr, Map<String, Object> params) throws BizException {
        Binding binding = new Binding(params);
        GroovyShell shell = new GroovyShell(binding);
        return (boolean) shell.evaluate(expr);
    }
}
