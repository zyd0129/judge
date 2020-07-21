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
    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRulePackageVersionDO convertToDo() {
        ConfigRulePackageVersionDO versionDO = new ConfigRulePackageVersionDO();
        setGmtModified(LocalDateTime.now());
        setGmtCreated(LocalDateTime.now());
        BeanUtils.copyProperties(this, versionDO);
        return versionDO;
    }
}
