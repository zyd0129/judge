package com.ps.judge.web.pojo.req;

import com.ps.judge.web.pojo.bo.ConfigRuleConditionBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class RuleConditionModifyReq {
    private Integer id;
    private String name;
    private String operator;
    private String operand;
    private String function;
    private String variableCode;
    private Integer variableType;
    private Integer status;

    public ConfigRuleConditionBO convertToBO() {
        ConfigRuleConditionBO bo = new ConfigRuleConditionBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
