package com.ps.common.query;

import com.ps.common.enums.Status;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
public class PackageQuery implements QueryConver {
    private String tenantCode;
    private String productCode;
    private String packageName;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private LocalDateTime gmtModifiedFrom;
    private LocalDateTime gmtModifiedTo;
    private String status;
    private int statusCode;

    @Override
    public void convert() {
        if (!StringUtils.isEmpty(status)) {
            setStatusCode(Status.valueOf(this.status).getValue());
        }
    }
}
