package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRulePackageVersionDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class ConfigRulePackageVersionBO {
    private Integer id;
    private Integer packageId;
    private Integer version;
    private Integer status;
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public static ConfigRulePackageVersionBO convertFromDo(ConfigRulePackageVersionDO versionDO) {
        ConfigRulePackageVersionBO versionBO = new ConfigRulePackageVersionBO();
        BeanUtils.copyProperties(versionDO,versionBO);
        return versionBO;
    }

    public ConfigRulePackageVersionDO convertToDo() {
        ConfigRulePackageVersionDO versionDO = new ConfigRulePackageVersionDO();
        if (id == null) {
            setGmtCreated(LocalDateTime.now());
        }
        setGmtModified(LocalDateTime.now());
        BeanUtils.copyProperties(this, versionDO);
        return versionDO;
    }
}
