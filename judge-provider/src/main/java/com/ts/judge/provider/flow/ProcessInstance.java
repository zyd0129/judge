package com.ts.judge.provider.flow;

import com.ts.judge.provider.enums.RunTimeStatus;
import com.ts.judge.provider.flow.bpmn.SimpleBPMDefinition;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.jury.api.request.ApplyRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class ProcessInstance {
    private Integer id;
    private Integer definitionId;
    private SimpleBPMDefinition definition;
    private NodeInstance currentNodeInstance;
    private String msg;

    /**
     * 流程变量
     */
    private Map<String, Object> processParams;
    /**
     * 流程入参
     */
    private ApplyRequest applyRequest;

    private RunTimeStatus status;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreated;
    private LocalDateTime CompletedTime;

    public void putProcessParam(String name, Object value) {
        if (processParams == null) {
            processParams = new HashMap<>();
        }
        processParams.put(name, value);
    }
}
