package com.ps.judge.api.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 风控结果查询
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/19
 */
@Data
public class AuditResultQuery {
    @NotBlank(message = "applyId 不能为空")
    private String applyId;
    @NotBlank(message = "tenantCode 不能为空")
    private String tenantCode;
}
