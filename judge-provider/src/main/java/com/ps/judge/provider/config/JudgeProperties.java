package com.ps.judge.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="com.ps.judge")
@Data
public class JudgeProperties {
	private String cronPush;
	private String cronRisk;
}