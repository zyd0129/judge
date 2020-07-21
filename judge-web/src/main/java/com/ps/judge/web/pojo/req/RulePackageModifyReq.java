package com.ps.judge.web.pojo.req;

import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RulePackageModifyReq {

    @NotNull
    private Integer id;

    private String name;

    private String code;

    private String tenantCode;

    public ConfigRulePackageBO convertToBO() {
        ConfigRulePackageBO bo = new ConfigRulePackageBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
