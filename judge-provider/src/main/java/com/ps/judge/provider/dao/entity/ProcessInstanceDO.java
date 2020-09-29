package com.ps.judge.provider.dao.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ProcessInstanceDO {
    private Integer id;
    private Integer definitionId;
    private String definition;

    private String currentNodeName;
    private String currentNodeStatus;

    private String processParams;
    private String applyRequest;
    private String status;
    private String msg;

    private Timestamp completedTime;
    private Timestamp gmtCreated;
    private Timestamp gmtModified;
}
