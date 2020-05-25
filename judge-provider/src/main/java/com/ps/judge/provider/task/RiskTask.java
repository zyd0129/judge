package com.ps.judge.provider.task;

import com.ps.judge.provider.service.JudgeService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class RiskTask extends QuartzJobBean {
	@Autowired
	JudgeService judgeService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

	}
}
