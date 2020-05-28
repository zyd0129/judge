package com.ps.judge.provider;

import com.alibaba.fastjson.JSONObject;
import com.ps.judge.api.entity.AuditResultVO;
import com.ps.jury.api.common.ApiResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JudgeProviderApplicationTests {

    @Test
    public void contextLoads() {
    }

}
