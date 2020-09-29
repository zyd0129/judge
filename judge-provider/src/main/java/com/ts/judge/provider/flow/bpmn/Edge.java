package com.ts.judge.provider.flow.bpmn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.rule.ActivationListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edge {
    private String start;
    private String end;
    private String expression;
}
