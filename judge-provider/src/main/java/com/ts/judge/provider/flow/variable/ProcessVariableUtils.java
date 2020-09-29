package com.ts.judge.provider.flow.variable;

import java.util.List;

public class ProcessVariableUtils {
    public static ProcessVariable findByName(List<ProcessVariable> variables, String name) {
        for (ProcessVariable v : variables) {
            if (v.getVarName().equals(name)) {
                return v;
            }
        }
        return null;
    }
}
