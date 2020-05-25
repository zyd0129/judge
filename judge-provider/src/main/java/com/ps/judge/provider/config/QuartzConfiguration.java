package com.ps.judge.provider.config;

import com.ps.judge.provider.task.PushTask;
import com.ps.judge.provider.task.RiskTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
	@Autowired
	private JudgeProperties properties;
	
	@Bean
    public JobDetail riskTaskDetail() {
		return JobBuilder.newJob(RiskTask.class).withIdentity("riskTask").storeDurably().build();
    }
	
	@Bean
	public Trigger riskTaskTaskTrigger() {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(properties.getCronRisk());
		return TriggerBuilder.newTrigger().forJob(riskTaskDetail()).withIdentity("riskTask")
				.withSchedule(scheduleBuilder).build();
	}

	@Bean
	public JobDetail pushTaskDetail() {
		return JobBuilder.newJob(PushTask.class).withIdentity("pushTask").storeDurably().build();
	}

	@Bean
	public Trigger pushTaskTrigger() {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(properties.getCronPush());
		return TriggerBuilder.newTrigger().forJob(pushTaskDetail()).withIdentity("pushTask")
				.withSchedule(scheduleBuilder).build();
	}

}
