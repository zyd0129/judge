package com.ts.judge.provider.dao.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProcessDefinitionDO {
    private Integer id;
    private String code;
    private Integer appId;
    private Integer tenantId;

    private String definition;

    private String operator;
    private Timestamp gmtCreated;
    private Timestamp gmtModified;
}
