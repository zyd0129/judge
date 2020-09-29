package com.ts.judge.provider.flow.bpmn;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.flow.node.type.StartNode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleBPMDefinitionTest {

    @Test
    public void bpmCase() {
        List<NodeInstance> nodeInstanceList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        NodeInstance startNode = new NodeInstance("startNode", "startNode", null, null, "1");
        NodeInstance endNode = new NodeInstance("endNode", "endNode", null, null, "2");

        nodeInstanceList.add(startNode);
        nodeInstanceList.add(endNode);

        Edge edge1 = new Edge("1","2","true");
        edgeList.add(edge1);
        SimpleBPMDefinition bpm = new SimpleBPMDefinition(nodeInstanceList,edgeList);

        System.out.println(JSONObject.toJSONString(bpm));

    }

    @Test
    public void grooyTest() {
        GroovyShell shell = new GroovyShell();
        System.out.println((boolean) shell.evaluate("true"));
    }
}