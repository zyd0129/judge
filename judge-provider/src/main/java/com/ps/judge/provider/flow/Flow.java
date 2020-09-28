package com.ps.judge.provider.flow;

import com.ps.judge.provider.flow.node.Node;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class Flow {

    // start-data-rule-data-end
    private List<Node> nodeList;
    private Node currentNode;
    private String currentStatus;

    private Map<String, Object> flowParams;

    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreated;
}
