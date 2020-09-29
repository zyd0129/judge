package com.ps.judge.provider.flow.variable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
