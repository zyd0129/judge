package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRulePackageDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ConfigRulePackageBO {
    private Integer id;
    private String tenantCode;
    private String code;
    private String name;
    private List<ConfigRulePackageVersionBO> versions;
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRulePackageBO() {
    }

    public static ConfigRulePackageBO convertFromDo(ConfigRulePackageDO doObj) {
        ConfigRulePackageBO configRulePackageBO = new ConfigRulePackageBO();
        BeanUtils.copyProperties(doObj, configRulePackageBO);
        if (doObj.getVersions() != null) {
            List<ConfigRulePackageVersionBO> versionBOS = doObj.getVersions().stream().map(ConfigRulePackageVersionBO::convertFromDo).collect(Collectors.toList());
            configRulePackageBO.setVersions(versionBOS);
        }
        return configRulePackageBO;
    }

    public ConfigRulePackageDO convertToDo() {
        ConfigRulePackageDO rulePackageDO = new ConfigRulePackageDO();
        if (id == null) {
            setGmtCreated(LocalDateTime.now());
        }
        setGmtModified(LocalDateTime.now());
        BeanUtils.copyProperties(this, rulePackageDO);
        return rulePackageDO;
    }
}
