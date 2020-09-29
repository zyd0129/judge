package com.ts.judge.provider.dao.ddao;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConfigRulePackageDO {
    private Integer id;
    private String tenantCode;
    private String code;
    private String name;
    private List<ConfigRulePackageVersionDO> versions;
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
