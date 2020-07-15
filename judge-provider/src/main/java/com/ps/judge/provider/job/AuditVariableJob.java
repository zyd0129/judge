package com.ps.judge.provider.job;

import com.ps.judge.provider.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时进行规则判断
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/19
 */
@DisallowConcurrentExecution
@Slf4j
public class AuditVariableJob extends QuartzJobBean {
    @Autowired
    ProcessService processService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("start auditVariable job");
        long startTime = System.currentTimeMillis();
        this.processService.auditVariable();
        long endTime = System.currentTimeMillis();
        log.info("end auditVariable job, time cost: {}", endTime - startTime);
    }
}