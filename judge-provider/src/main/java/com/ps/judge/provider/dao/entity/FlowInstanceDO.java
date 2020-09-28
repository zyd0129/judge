package com.ps.judge.provider.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlowInstanceDO {
    private Integer id;
    private Integer flowId;

    private String  nodeList;
    private Integer currentIndex;
    private String  currentNodeName;
    private String currentNodeStatus;

    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
