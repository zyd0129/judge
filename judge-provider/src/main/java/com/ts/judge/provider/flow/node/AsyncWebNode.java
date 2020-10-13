package com.ts.judge.provider.flow.node;


import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.flow.script.DefaultJSONOutputScript;
import com.ts.judge.provider.flow.script.IOutputScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AsyncWebNode extends AsyncNode {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException {
        Map<String, Object> properties = nodeInstance.getProperties();
        String url = (String) properties.get("url");
        String outputScriptStr = (String) properties.get("outputScript");
        String inputScriptStr = (String) properties.get("inputScript");
        Map<String, Object> inputParams = new HashMap<>();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, inputParams, String.class);

        String body = responseEntity.getBody();
        //加载outputScript
        IOutputScript outputScript = new DefaultJSONOutputScript();
        Object result = outputScript.process(body);
        flowInstance.putProcessParam(nodeInstance.getName(), result);
    }
    @Override
    public NodeResult callback(ProcessInstance flowInstance, Map<String, Object> params) {
        return null;
    }

    @Override
    public String getName() {
        return "AsyncWebNode";
    }
}
