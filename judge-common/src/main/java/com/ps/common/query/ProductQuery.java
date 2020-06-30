package com.ps.common.query;

import com.ps.common.enums.Status;
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
    private int statusCode;
    private Boolean fuzzy = false;

    @Override
    public void convert() {
        fuzzy = false;
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
        if (!StringUtils.isEmpty(status)) {
            setStatusCode(Status.valueOf(this.status).getValue());
        }
    }
}
