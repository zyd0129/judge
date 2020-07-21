package com.ps.judge.web.pojo.req;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseReq {
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
