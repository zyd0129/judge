package com.ps.judge.web.pojo.req;

import com.ps.judge.web.models.ConfigProductBO;
import com.ps.judge.web.pojo.bo.ConfigRulePackageVersionBO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RulePackageVersionModifyReq {

    @NotNull
    private Integer id;
    @NotNull
    private Integer status;

    public ConfigRulePackageVersionBO convertToBO() {
        ConfigRulePackageVersionBO bo = new ConfigRulePackageVersionBO();
        BeanUtils.copyProperties(this, bo);
        return bo;
    }
}
