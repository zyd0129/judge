package com.ps.judge.web.pojo.req;

import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class RulePackageReq extends BaseReq {

    @NotBlank
    private String name;
    @NotBlank
    private String code;
    @NotBlank
    private String tenantCode;

    public ConfigRulePackageBO convertToBO() {
        ConfigRulePackageBO bo = new ConfigRulePackageBO();
        setGmtCreated(LocalDateTime.now());
        setGmtModified(LocalDateTime.now());
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
