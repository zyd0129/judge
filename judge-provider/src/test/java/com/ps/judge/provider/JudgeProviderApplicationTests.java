package com.ps.judge.provider;

import com.ps.jury.api.JuryApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JudgeProviderApplicationTests {
    @Autowired
    JuryApi juryApi;

    @Test
    public void contextLoads() {
        System.err.println(this.juryApi.getVarResult("12", "12"));
    }
}
