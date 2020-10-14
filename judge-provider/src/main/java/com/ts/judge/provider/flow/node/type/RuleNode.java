package com.ts.judge.provider.flow.node.type;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.exceptions.ProcessException;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.flow.rule.RuleEngine;
import com.ts.judge.provider.flow.rule.RulePackage;
import com.ts.judge.provider.flow.rule.RulePackageResult;
import com.ts.judge.provider.service.RulePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ruleNode")
public class RuleNode extends Node {

    @Autowired
    RulePackageService rulePackageService;

    @Autowired
    RuleEngine ruleEngine;

    @Override
    protected Map<String, Object> process(Map<String, Object> inputParams, NodeInstance nodeInstance) throws ProcessException {
        Map<String, Object> properties = nodeInstance.getProperties();
        Integer rulePackageId = (Integer) properties.getOrDefault("rulePackageId", null);
        if (rulePackageId == null) {
            throw new ProcessException(70003, "ruleNode 缺少 rulePackageId");
        }
        RulePackage rulePackage = rulePackageService.getById(rulePackageId);
        RulePackageResult rulePackageResult = ruleEngine.execute(rulePackage, inputParams);
        rulePackageService.insert((Integer) inputParams.get("processInstanceId"), rulePackageResult);
        return JSONObject.parseObject(JSONObject.toJSONString(rulePackageResult));
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public String getName() {
        return "rule";
    }
}
