package com.ps.common.query;

import com.ps.common.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskQuery {
    private Integer id;
    private String tenantCode;
    private String userName;
    private String mobile;
    private String idCard;
    private LocalDateTime completeTimeFrom;
    private LocalDateTime completeTimeTo;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private TaskStatus status;
    private List<Integer> taskStatus;
}
