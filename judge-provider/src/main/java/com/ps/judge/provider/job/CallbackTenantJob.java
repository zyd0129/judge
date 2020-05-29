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
public class CallbackTenantJob extends QuartzJobBean {
	@Autowired
    ProcessService processService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("start callbackTenant job ");
		long startTime = System.currentTimeMillis();
		this.processService.callbackTenant();
		long endTime = System.currentTimeMillis();
		log.info("end callbackTenant job, time cost: {}", endTime - startTime);
	}
}