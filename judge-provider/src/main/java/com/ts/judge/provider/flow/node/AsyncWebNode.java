package com.ts.judge.provider.flow.node;


import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.flow.script.DefaultOutputScript;
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
    protected Map<String, Object> process(Map<String, Object> inputParams, NodeInstance nodeInstance) throws ProcessException {
        return null;
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
