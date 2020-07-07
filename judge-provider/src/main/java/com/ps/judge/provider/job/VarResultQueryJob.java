package com.ps.judge.provider.job;

import com.ps.judge.provider.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时请求jury，查询变量结果
 *
 * @author ：zhangqian9044.
 * @date ：2020/5/19
 */
@DisallowConcurrentExecution
@Slf4j
public class VarResultQueryJob extends QuartzJobBean {
    @Autowired
    ProcessService processService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start varResultQuery job");
        long startTime = System.currentTimeMillis();
        this.processService.varResultQuery();
        long endTime = System.currentTimeMillis();
        log.info("end varResultQuery job, time cost: {}", endTime - startTime);
    }
}
