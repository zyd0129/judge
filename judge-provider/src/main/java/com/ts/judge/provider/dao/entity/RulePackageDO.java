package com.ts.judge.provider.dao.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RulePackageDO {

    private Integer id;
    private String name;
    private List<RuleDO> ruleDOList;

    private String operator;
    private Timestamp gmtCreated;
    private Timestamp gmtModified;
}
