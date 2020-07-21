package com.ps.judge.web.pojo.req;

import com.ps.judge.web.pojo.bo.ConfigRuleBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;


@Data
public class RuleCreateReq {
    private String tenantCode;
    private Integer rulePackageVersionId;
    private String code;
    private String name;
    /**
     * 优先级
     */
    private Integer salience;
    private Integer score;
    private String result;

    public ConfigRuleBO convertToBO() {
        ConfigRuleBO bo = new ConfigRuleBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
