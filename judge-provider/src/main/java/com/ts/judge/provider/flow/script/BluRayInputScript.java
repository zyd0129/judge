package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.third.bluRay.BluRayClient;
import com.ts.judge.provider.third.bluRay.BluRayParams;
import com.ts.judge.provider.third.txScore.ModelParams;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public class BluRayInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ProcessInstance processInstance) {
        BluRayClient bluRayClient = new BluRayClient();
        BluRayParams bluRayParams = new BluRayParams();
        bluRayClient.send(bluRayParams);
        return null;
    }
}
