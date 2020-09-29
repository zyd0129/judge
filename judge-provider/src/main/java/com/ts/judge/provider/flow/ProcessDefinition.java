package com.ts.judge.provider.flow;

import com.ts.judge.provider.flow.bpmn.SimpleBPMDefinition;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessDefinition {

    private Integer id;
    private String code;
    private Integer appId;
    private Integer tenantId;

    /**
     * 从definition里解析
     */
    private SimpleBPMDefinition definition;

    private String operator;

    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreated;
}
