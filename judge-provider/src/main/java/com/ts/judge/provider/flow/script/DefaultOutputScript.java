package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class DefaultOutputScript implements IOutputScript{

    @Override
    public Map<String, Object> process(Map<String, Object> output) {
        return output;
    }
}
