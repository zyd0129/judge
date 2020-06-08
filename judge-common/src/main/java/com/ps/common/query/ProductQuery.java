package com.ps.common.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductQuery {
    /**
     * 商户名称，产品编号，产品名称
     */
    private String fuzzyValue;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private LocalDateTime gmtModifiedFrom;
    private LocalDateTime gmtModifiedTo;
    private String status;
}
