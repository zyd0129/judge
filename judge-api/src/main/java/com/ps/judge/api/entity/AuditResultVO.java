package com.ps.judge.api.entity;

import com.ps.jury.api.header.JuryHeader;
import lombok.Data;

import java.util.List;

/**
 * 风控结果
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/18
 */
@Data
public class AuditResultVO extends JuryHeader {
    private Integer taskStatus;
    private Integer auditScore;
    private String auditCode;
    private List<NodeResultVO> nodeResult;
}
