package com.ts.judge.provider.flow.node;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.script.IInputScript;
import com.ts.judge.provider.flow.script.IOutputScript;
import com.ts.judge.provider.flow.script.ScriptCache;
import com.ts.jury.api.request.ApplyRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Data
@Slf4j
public abstract class Node {

    @Autowired
    ScriptCache scriptCache;

    public NodeResult execute(ProcessInstance flowInstance, NodeInstance nodeInstance) {
        Map<String, Object> properties = nodeInstance.getProperties();
        String outputScriptStr = null;
        String inputScriptStr = null;
        if (properties != null) {
            outputScriptStr = (String) properties.getOrDefault("outputScript", null);
            inputScriptStr = (String) properties.getOrDefault("inputScript", null);
        }

        NodeResult nodeResult = new NodeResult();
        try {
            IInputScript inputScript = scriptCache.getInputScript(flowInstance.getDefinitionId(), nodeInstance.getName(), inputScriptStr);
            IOutputScript outputScript = scriptCache.getOutputScript(flowInstance.getDefinitionId(), nodeInstance.getName(), outputScriptStr);
            Map<String, Object> inputParams = inputScript.parse(flowInstance);
            Map<String, Object> rawResult = process(inputParams, nodeInstance);
            if (rawResult != null) {
                Map<String, Object> processResult = outputScript.process(rawResult);
                if (processResult != null) {
                    flowInstance.putProcessParam(nodeInstance.getName(), processResult);
                }
            }
            nodeResult.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            nodeResult.setSuccess(false);
            nodeResult.setMsg(e.getMessage());
        }
        return nodeResult;
    }

    protected abstract Map<String, Object> process(Map<String, Object> inputParams, NodeInstance nodeInstance) throws ProcessException;

    public abstract boolean isSync();

    public abstract String getName();
}
