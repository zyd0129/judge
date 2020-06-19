package com.ps.common.query;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class ProductQuery implements QueryConver {
    private String tenantName;
    private String productName;
    private String productCode;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private LocalDateTime gmtModifiedFrom;
    private LocalDateTime gmtModifiedTo;
    private String status;
    private Boolean fuzzy;

    @Override
    public void convert() {
        if (!StringUtils.isEmpty(productCode)) {
            setProductCode("%" + productCode + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(tenantName)) {
            setTenantName("%" + tenantName + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(productName)) {
            setProductName("%" + productName + "%");
            fuzzy = true;
        }
    }
}
