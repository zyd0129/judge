package com.ts.judge.provider.flow.node;


import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.flow.script.DefaultInputScript;
import com.ts.judge.provider.flow.script.DefaultOutputScript;
import com.ts.judge.provider.flow.script.IInputScript;
import com.ts.judge.provider.flow.script.IOutputScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 仅支持返还map的json
 */
@Service
@Slf4j
public class WebNode extends Node {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException {
    }

    @Override
    protected Map<String, Object> process(Map<String, Object> inputParams, NodeInstance nodeInstance) throws ProcessException {
        Map<String, Object> properties = nodeInstance.getProperties();
        String url = (String) properties.get("url");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, inputParams, String.class);
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new ProcessException(70002, url + "调用失败");
        }
        String body = responseEntity.getBody();
        return JSONObject.parseObject(body);
    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public String getName() {
        return "webNode";
    }
}
