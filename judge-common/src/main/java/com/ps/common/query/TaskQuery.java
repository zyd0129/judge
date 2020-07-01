package com.ps.common.query;

import com.ps.common.enums.TaskStatus;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class TaskQuery implements QueryConver {
    private List<Integer> successStatus = Arrays.asList(6, 8, 9, 10);
    private List<Integer> runningStatus = Arrays.asList(0, 1, 2, 3, 4, 5);
    List<Integer> failureStatus = Collections.singletonList(7);

    private Integer id;
    private String tenantCode;
    private String applyId;
    private String mobile;
    private String idCard;
    private LocalDateTime completeTimeFrom;
    private LocalDateTime completeTimeTo;
    private LocalDateTime gmtCreatedFrom;
    private LocalDateTime gmtCreatedTo;
    private TaskStatus status;
    private List<Integer> taskStatus;
    private Boolean fuzzy = false;

    @Override
    public void convert() {
        fuzzy = false;
        if (!StringUtils.isEmpty(tenantCode)) {
            setTenantCode("%" + tenantCode + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(applyId)) {
            setApplyId("%" + applyId + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(mobile)) {
            setMobile("%" + mobile + "%");
            fuzzy = true;
        }
        if (!StringUtils.isEmpty(idCard)) {
            setIdCard("%" + idCard + "%");
            fuzzy = true;
        }
        if (TaskStatus.SUCCESS.equals(status)) {
            setTaskStatus(successStatus);
        } else if (TaskStatus.FAILURE.equals(status)) {
            setTaskStatus(failureStatus);
        } else if (TaskStatus.RUNNING.equals(status)) {
            setTaskStatus(runningStatus);
        } else {
            setTaskStatus(null);
        }
    }
}
