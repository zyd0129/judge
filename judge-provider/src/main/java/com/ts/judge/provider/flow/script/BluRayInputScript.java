package com.ts.judge.provider.flow.script;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.third.txScore.ModelParams;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public class TxScoreInputScript implements IInputScript {
    @Override
    public Map<String, Object> parse(ApplyRequest applyRequest, Map<String, Object> processParams) {
        String s = "{\"education\":\"High school\",\"app_amount\":57,\"sex\":\"Male\",\"working_years\":\"more than 5 years\",\"in_big_city\":0,\"salary\":20000.0,\"occupation_class\":\"Manager\",\"real_contact\":261,\"financial_app\":16,\"register_addr\":\"Unnamed Road, Srikaranpur, Rajasthan 335073, India\",\"regist_time\":\"2020-09-23\",\"date_birthday\":\"1992-05-03\",\"contact\":277,\"income_by\":\"Cash\",\"marry_state\":\"Unmarried\",\"supplement_state\":0}";
        ModelParams modelParams = JSONObject.parseObject(s, ModelParams.class);
        processParams.put("applyRequest",applyRequest);
        return processParams;
    }
}
