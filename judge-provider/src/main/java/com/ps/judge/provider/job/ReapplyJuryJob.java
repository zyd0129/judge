package com.ps.judge.provider.job;

import com.ps.judge.provider.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
@Slf4j
public class ReapplyJuryJob extends QuartzJobBean {
    @Autowired
    ProcessService processService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start applyJury job");
        long startTime = System.currentTimeMillis();
        this.processService.reapplyJury();
        long endTime = System.currentTimeMillis();
        log.info("start applyJury job, time cost: {}", endTime - startTime);
    }
}
