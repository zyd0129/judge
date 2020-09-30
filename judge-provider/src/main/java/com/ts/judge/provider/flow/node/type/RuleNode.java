package com.ts.judge.provider.flow.node.type;

import com.ts.judge.provider.flow.ProcessInstance;
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
    public void process(ProcessInstance flowInstance, NodeInstance nodeInstance) throws ProcessException {
        Map<String, Object> properties = nodeInstance.getProperties();
        Integer rulePackageId = (Integer) properties.getOrDefault("rulePackageId", null);
        if (rulePackageId == null) {
            throw new ProcessException(70003, "ruleNode 缺少 rulePackageId");
        }
        RulePackage rulePackage = rulePackageService.getById(rulePackageId);
        RulePackageResult rulePackageResult = ruleEngine.execute(rulePackage,flowInstance.getProcessParams());
        rulePackageService.insert(flowInstance.getId(), rulePackageResult);
        flowInstance.putProcessParam(nodeInstance.getName(),rulePackageResult);
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
