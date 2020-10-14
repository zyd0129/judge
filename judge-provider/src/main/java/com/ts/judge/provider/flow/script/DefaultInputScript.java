package com.ts.judge.provider.flow.script;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ts.judge.provider.flow.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

public class DefaultInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        Map<String, Object> result = null;
        //需要对processParams进行克隆
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String s = objectMapper.writeValueAsString(processInstance.getApplyRequest());
            result = objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
