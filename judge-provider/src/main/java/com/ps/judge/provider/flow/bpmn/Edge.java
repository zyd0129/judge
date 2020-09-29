package com.ps.judge.provider.flow.bpmn;

import lombok.Data;

@Data
public class Edge {
    private String start;
    private String end;
    private String expression;
}
