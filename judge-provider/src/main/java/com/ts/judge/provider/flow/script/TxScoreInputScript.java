package com.ts.judge.provider.flow.script;

import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public class DefaultInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ApplyRequest applyRequest, Map<String, Object> processParams) {
        processParams.put("applyRequest",applyRequest);
        return processParams;
    }
}
