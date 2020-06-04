package com.ps.judge.provider.config;

import com.ps.judge.provider.job.AuditVariableJob;
import com.ps.judge.provider.job.ReapplyJuryJob;
import com.ps.judge.provider.job.CallbackTenantJob;
import com.ps.judge.provider.job.VarResultQueryJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
    @Autowired
    private JudgeProviderProperties properties;

    @Bean
    public JobDetail callbackTenantJobDetail() {
        return JobBuilder.newJob(CallbackTenantJob.class).withIdentity("callbackTenantJob").storeDurably().build();
    }

    @Bean
    public Trigger callbackTenantJobTrigger() {
        System.out.println(this.properties.getCronCallbackTenant());
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

    @Bean
    public JobDetail auditVariableJobDetail() {
        return JobBuilder.newJob(AuditVariableJob.class).withIdentity("auditVariableJob").storeDurably().build();
    }

    @Bean
    public Trigger auditVariableJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.properties.getCronAuditVariable());
        return TriggerBuilder.newTrigger().forJob(auditVariableJobDetail()).withIdentity("auditVariableJob")
                .withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail varResultQueryJobDetail() {
        return JobBuilder.newJob(VarResultQueryJob.class).withIdentity("varResultQueryJob").storeDurably().build();
    }

    @Bean
    public Trigger varResultQueryJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(this.properties.getCronVarResultQuery());
        return TriggerBuilder.newTrigger().forJob(varResultQueryJobDetail()).withIdentity("varResultQueryJob")
                .withSchedule(scheduleBuilder).build();
    }
}
