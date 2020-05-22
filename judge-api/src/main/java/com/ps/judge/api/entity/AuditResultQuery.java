package com.ps.judge.api.entity;

import lombok.Data;

/**
 * 风控结果查询
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/19
 */
@Data
public class AuditResultQuery {
    private String applyId;
    private String tenantId;
}
