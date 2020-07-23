package com.ps.judge.web.pojo.req;

import com.ps.judge.web.pojo.bo.ConfigRulePackageBO;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RulePackageVersionCreateReq {

    @NotNull
    private Integer rulePackageId;

    public ConfigRulePackageVersionBO convertToBO() {
        ConfigRulePackageVersionBO bo = new ConfigRulePackageVersionBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
