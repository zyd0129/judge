package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRuleDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ConfigRuleBO {
    private Integer id;
    private String tenantCode;
    private Integer rulePackageVersionId;
    private String code;
    private String name;
    private Integer version;
    /**
     * 优先级
     */
    private Integer salience;
    private Integer status;
    private Integer score;
    private String result;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRuleDO convertToDo() {
        ConfigRuleDO ruleDO = new ConfigRuleDO();
        if (id == null) {
            setGmtCreated(LocalDateTime.now());
        }
        setGmtModified(LocalDateTime.now());
        BeanUtils.copyProperties(this,ruleDO);
        return ruleDO;
    }

    public static ConfigRuleBO convertFromDo(ConfigRuleDO ruleDO) {
        ConfigRuleBO ruleBO = new ConfigRuleBO();
        BeanUtils.copyProperties(ruleDO, ruleBO);
//        if (aDo.getVersions() != null) {
//            List<ConfigRulePackageVersionBO> versionBOS = aDo.getVersions().stream().map(ConfigRulePackageVersionBO::convertFromDo).collect(Collectors.toList());
//            ruleBO.setVersions(versionBOS);
//        }
        return ruleBO;
    }
}
