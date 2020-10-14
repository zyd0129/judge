package com.ts.judge.provider.flow.script;

import java.util.HashMap;
import java.util.Map;

public class TxScoreOutputScript implements IOutputScript {
    @Override
    public Map<String, Object> process(Map<String, Object> output) {
        Integer point = (Integer) output.getOrDefault("point", -99);
        Map<String, Object> processResult = new HashMap<>();
        processResult.put("point", point);
        return processResult;
    }
}
