package com.ps.judge.web.pojo.bo;

import com.ps.judge.dao.entity.ConfigRuleVariableDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class ConfigRuleVariableBO {
    private Integer id;
    private String name;
    private String code;
    private String level;
    private String group;
    private String type;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public static ConfigRuleVariableBO convertFromDo(ConfigRuleVariableDO variableDO) {
        ConfigRuleVariableBO variableBO = new ConfigRuleVariableBO();
        BeanUtils.copyProperties(variableDO, variableBO);
        return variableBO;
    }
}
