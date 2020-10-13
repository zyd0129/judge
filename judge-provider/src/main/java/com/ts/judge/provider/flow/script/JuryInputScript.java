package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.third.bluRay.BluRayClient;
import com.ts.judge.provider.third.bluRay.BluRayParams;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public class RuleInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        //需要对processParams进行克隆
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(processInstance.getProcessParams()));
        jsonObject.put("processInstanceId", processInstance.getId());
        return jsonObject;
    }
}
