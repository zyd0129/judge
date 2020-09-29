package com.ps.judge.provider.flow.bpmn;

import com.ps.judge.provider.flow.node.runtime.NodeInstance;
import com.ps.judge.provider.flow.node.StartNode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private NodeInstance getNodeInstanceById(String nodeId) {
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
        return edgeList.stream().filter(edge -> nodeId.equals(edge.getStart())).collect(Collectors.toList());
    }


    public NodeInstance getStartNodeInstance() {
        for (NodeInstance instance : nodeInstanceList) {
            if (instance.getNode() instanceof StartNode) {
                return instance;
            }
        }
        return null;
    }
}
