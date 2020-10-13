package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import com.ts.jury.api.dto.VariableResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JuryOutputScript implements IOutputScript {
    @Override
    public Map<String, Object> process(Map<String, Object> output) {
        boolean success = (boolean) output.getOrDefault("success", false);
        if (!success) {
            return null;
        }
        List data = (List) output.getOrDefault("data", null);

        /**
         * 基本类型，restTemplate已经自动解析
         * Object类型，json字符串
         */

        Map<String, Object> juryVariable = new HashMap<>();
        data.forEach(variableResult -> {
            Map<String, Object> variableResultMap = (Map<String, Object>) variableResult;
            String varName = (String) variableResultMap.get("varName");
            Object varValue = variableResultMap.getOrDefault("varValue", null);
            String varType = (String) variableResultMap.get("varType");
            if (varValue == null) {
                varValue = variableResultMap.getOrDefault("varDefaultValue", null);
            }
            if ("Object".equals(varType)) {
                varValue = JSONObject.parse(varValue.toString());
            }
            juryVariable.put(varName, varValue);
        });
        return juryVariable;
    }
}
