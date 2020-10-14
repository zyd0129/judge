package com.ts.judge.provider.flow.script;

import com.ts.judge.provider.flow.ProcessInstance;

import java.util.Map;

public class JuryInputScript extends DefaultInputScript implements IInputScript{
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        Map<String, Object> result = super.parse(processInstance);
        result.put("applyRequest", processInstance.getApplyRequest());
        return  result;
    }
}
