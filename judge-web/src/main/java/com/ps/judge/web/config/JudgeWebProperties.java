package com.ps.judge.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "com.ps.judge.web")
public class JudgeWebProperties {
    private String bucketName;
    private String baseUrl;
    private long expiration;
}
