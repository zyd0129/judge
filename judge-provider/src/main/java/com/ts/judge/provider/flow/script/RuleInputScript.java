package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.third.bluRay.BluRayClient;
import com.ts.judge.provider.third.bluRay.BluRayParams;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public class RuleInputScript extends DefaultInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        Map<String, Object> result = super.parse(processInstance);
        result.put("processInstanceId", processInstance.getId());
        return result;
    }
}
