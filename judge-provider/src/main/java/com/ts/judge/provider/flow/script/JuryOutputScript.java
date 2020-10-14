package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JuryOutputScript implements IOutputScript {
    private final Logger logger = LoggerFactory.getLogger(JuryOutputScript.class);

    @Override
    public Map<String, Object> process(Map<String, Object> output) {
        logger.info("jury结果:{}", JSONObject.toJSONString(output));
        boolean success = (boolean) output.getOrDefault("success", false);
        if (!success) {
            logger.error("jury服务出错");
            return null;
        }
        List data = (List) output.getOrDefault("data", null);

        /**
         * 基本类型，restTemplate已经自动解析
         * Object类型，json字符串
         */

        Map<String, Object> juryVariable = new HashMap<>();
        for (Object variableResult : data) {
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
        }
        return juryVariable;
    }
}
