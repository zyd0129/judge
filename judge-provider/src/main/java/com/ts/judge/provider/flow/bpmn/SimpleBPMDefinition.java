package com.ts.judge.provider.flow.bpmn;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ts.judge.provider.flow.node.NodeInstance;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SimpleBPMDefinition {

    private List<NodeInstance> nodeInstanceList;
    private List<Edge> edgeList;

    public SimpleBPMDefinition(List<NodeInstance> nodeInstanceList, List<Edge> edgeList) {
        this.nodeInstanceList = nodeInstanceList;
        this.edgeList = edgeList;
    }

    public NodeInstance nextNodeInstance(String nodeId, Map<String, Object> processVariables) {
        NodeInstance nodeInstance;
        List<Edge> edges = nextEdges(nodeId);
        if (edges == null || edges.size() == 0) {
            nodeInstance = null;
        } else {
            nodeInstance = selectEdge(edges, processVariables);
        }
        return nodeInstance;
    }

    private NodeInstance selectEdge(List<Edge> edges, Map<String, Object> processVariables) {
        for (Edge edge : edges) {
            if (evaluate(edge, processVariables)) {
                return getNodeInstanceById(edge.getEnd());
            }
        }
        return null;
    }

    public NodeInstance getNodeInstanceById(String nodeId) {
        if(nodeInstanceList==null) {
            return null;
        }
        for (NodeInstance instance : nodeInstanceList) {
            if (instance.getNodeInstanceId().equals(nodeId)) {
                return instance;
            }
        }
        return null;
    }

    private boolean evaluate(Edge edge, Map<String, Object> processVariables) {
        Binding binding = new Binding(processVariables);
        GroovyShell shell = new GroovyShell(binding);

        return (boolean) shell.evaluate(edge.getExpression());
    }

    private List<Edge> nextEdges(String nodeId) {
        if (edgeList == null) {
            return null;
        }
        return edgeList.stream().filter(edge -> nodeId.equals(edge.getStart())).collect(Collectors.toList());
    }


    @JsonIgnore
    @JSONField(serialize = false)
    public NodeInstance getStartNodeInstance() {
        if(nodeInstanceList==null){
            return null;
        }
        for (NodeInstance instance : nodeInstanceList) {
            if ("startNode".equals(instance.getNodeType())) {
                return instance;
            }
        }
        return null;
    }
}
