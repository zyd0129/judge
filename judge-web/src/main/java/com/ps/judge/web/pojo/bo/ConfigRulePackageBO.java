package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRulePackageDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class ConfigRulePackageBO {
    private Integer id;
    private String tenantCode;
    private String code;
    private String name;
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRulePackageBO() {
    }

    public static ConfigRulePackageBO convertFromDo(ConfigRulePackageDO doObj) {
        ConfigRulePackageBO configRulePackageBO = new ConfigRulePackageBO();
        BeanUtils.copyProperties(doObj, configRulePackageBO);
        return configRulePackageBO;
    }

    public ConfigRulePackageDO convertToDo() {
        ConfigRulePackageDO rulePackageDO = new ConfigRulePackageDO();
        setGmtModified(LocalDateTime.now());
        setGmtCreated(LocalDateTime.now());
        BeanUtils.copyProperties(this, rulePackageDO);
        return rulePackageDO;
    }
}
