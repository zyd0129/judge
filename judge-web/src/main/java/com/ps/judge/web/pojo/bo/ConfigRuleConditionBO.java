package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRuleConditionDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class ConfigRuleConditionBO {
    private Integer id;
    private Integer ruleId;
    private String name;
    private String operator;
    private String operand;
    private String function;
    private String variableCode;
    private Integer variableType;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRuleConditionDO convertToDo() {

        ConfigRuleConditionDO conditionDO = new ConfigRuleConditionDO();
        if (id == null) {
            setGmtCreated(LocalDateTime.now());
        }
        setGmtModified(LocalDateTime.now());
        BeanUtils.copyProperties(this, conditionDO);
        return conditionDO;
    }

    public static ConfigRuleConditionBO convertFromDo(ConfigRuleConditionDO conditionDO) {
        ConfigRuleConditionBO conditionBO = new ConfigRuleConditionBO();
        BeanUtils.copyProperties(conditionDO, conditionBO);
        return conditionBO;
    }
}