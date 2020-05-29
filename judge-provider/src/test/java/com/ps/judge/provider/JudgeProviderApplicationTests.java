package com.ps.judge.provider;

import com.ps.judge.dao.entity.AuditTaskDO;
import com.ps.judge.provider.service.ProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JudgeProviderApplicationTests {
    @Autowired
    ProcessService processService;

    @Test
    public void contextLoads() {
        AuditTaskDO auditTask = this.processService.getAuditTask("1002", "1001");
    }

}
