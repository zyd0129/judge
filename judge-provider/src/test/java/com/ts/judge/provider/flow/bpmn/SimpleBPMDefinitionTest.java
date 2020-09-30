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
        NodeInstance juryNode = new NodeInstance("juryNode", "juryNode", null, null, "2");
        NodeInstance endNode = new NodeInstance("endNode", "endNode", null, null, "3");

        nodeInstanceList.add(startNode);
        nodeInstanceList.add(juryNode);
        nodeInstanceList.add(endNode);

        Edge edge1 = new Edge("1","2","true");
        Edge edge2 = new Edge("2","3","true");
        edgeList.add(edge1);
        edgeList.add(edge2);
        SimpleBPMDefinition bpm = new SimpleBPMDefinition(nodeInstanceList,edgeList);

        System.out.println(JSONObject.toJSONString(bpm));

    }

    @Test
    public void grooyTest() {
        GroovyShell shell = new GroovyShell();
        System.out.println((boolean) shell.evaluate("true"));
    }
}