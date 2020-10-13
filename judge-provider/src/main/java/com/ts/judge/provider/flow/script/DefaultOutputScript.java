package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class DefaultJSONOutputScript implements IOutputScript{
    @Override
    public Object process(String body) {
        return JSONObject.parse(body);
    }
}
