package com.ps.common.query;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class FlowQuery implements QueryConver {
    private String tenantCode;
    private String productCode;
    private String packageName;
    private String flowName;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private String status;
    private Boolean fuzzy = false;

    @Override
    public void convert() {
        fuzzy = false;
        if (!StringUtils.isEmpty(flowName)) {
            setTenantCode("%" + flowName + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(packageName)) {
            setPackageName("%" + packageName + "%");
            fuzzy = true;
        }
    }
}
