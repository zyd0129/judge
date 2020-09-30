package com.ts.judge.provider.flow.node.type;

import com.alibaba.fastjson.JSONObject;
import com.ts.clerk.common.response.ApiResponse;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.jury.api.JuryApi;
import com.ts.jury.api.dto.VariableResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("juryNode")
@Slf4j
public class JuryNode extends Node {

    @Autowired
    JuryApi juryApi;

    @Override
    public void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException {
        ApiResponse<List<VariableResult>> apiResponse = juryApi.apply(flowInstance.getApplyRequest());

        log.info("processInstance{}, jury result:{}",flowInstance.getId(), JSONObject.toJSONString(apiResponse));

        if (!apiResponse.isSuccess()) {
            throw new ProcessException(70002, "jury调用失败");
        }
        List<VariableResult> data = apiResponse.getData();

        data.forEach(variableResult -> {
            if (!variableResult.isVarHasError()) {
                String varName = variableResult.getVarName();
                Object varValue = variableResult.getVarValue();
                if (varValue == null) {
                    varValue = variableResult.getVarDefaultValue();
                }
                Map<String, Object> processParams = flowInstance.getProcessParams();
                if (processParams == null) {
                    processParams = new HashMap<>();
                    flowInstance.setProcessParams(processParams);
                }
                processParams.put(varName, varValue);

            }
        });
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "jury";
    }
}
