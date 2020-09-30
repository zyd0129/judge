package com.ts.judge.provider.flow.node.type;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.third.txScore.ModelParams;
import com.ts.judge.provider.third.txScore.ModelResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service("txScoreNode")
@Slf4j
public class TxScoreNode extends Node {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void process(ProcessInstance flowInstance) throws ProcessException {
        String s = "{\"education\":\"High school\",\"app_amount\":57,\"sex\":\"Male\",\"working_years\":\"more than 5 years\",\"in_big_city\":0,\"salary\":20000.0,\"occupation_class\":\"Manager\",\"real_contact\":261,\"financial_app\":16,\"register_addr\":\"Unnamed Road, Srikaranpur, Rajasthan 335073, India\",\"regist_time\":\"2020-09-23\",\"date_birthday\":\"1992-05-03\",\"contact\":277,\"income_by\":\"Cash\",\"marry_state\":\"Unmarried\",\"supplement_state\":0}";
        ModelParams modelParams = JSONObject.parseObject(s,ModelParams.class);
        ModelResult ap = restTemplate.postForObject("https://apitest.wificash.in/python/Xg_score", modelParams, ModelResult.class);
        log.info("processInstance{}, tsScore result:{}",flowInstance.getId(), JSONObject.toJSONString(ap));
        if (ap == null) {
            throw new ProcessException(70003, "txScore调用失败");
        }
        flowInstance.putProcessParam("txScore", ap.getPoint());

    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "txScore";
    }
}
