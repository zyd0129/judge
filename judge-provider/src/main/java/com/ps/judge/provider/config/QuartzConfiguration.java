package com.ps.judge.provider.config;

import com.ps.judge.provider.job.ReapplyJuryJob;
import com.ps.judge.provider.job.CallbackTenantJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
	@Autowired
	private JudgeProperties properties;
	
	@Bean
    public JobDetail callbackTenantJobDetail() {
		return JobBuilder.newJob(CallbackTenantJob.class).withIdentity("callbackTenantJob").storeDurably().build();
    }
	
	@Bean
	public Trigger auditResultCallbackJobTrigger() {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.properties.getCronCallbackTenant());
		return TriggerBuilder.newTrigger().forJob(callbackTenantJobDetail()).withIdentity("callbackTenantJob")
				.withSchedule(scheduleBuilder).build();
	}

	@Bean
	public JobDetail reapplyJuryJobDetail() {
		return JobBuilder.newJob(ReapplyJuryJob.class).withIdentity("reapplyJuryJob").storeDurably().build();
	}

	@Bean
	public Trigger reapplyJuryJobTrigger() {
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.properties.getCronReapplyJury());
		return TriggerBuilder.newTrigger().forJob(reapplyJuryJobDetail()).withIdentity("reapplyJuryJob")
				.withSchedule(scheduleBuilder).build();
	}

}
