package com.ps.judge.web.pojo.req;

import com.fasterxml.jackson.annotation.JsonView;
import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class RulePackageCreateReq {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String tenantCode;

    private Integer operatorId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public ConfigRulePackageBO convertToBO() {
        ConfigRulePackageBO bo = new ConfigRulePackageBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
