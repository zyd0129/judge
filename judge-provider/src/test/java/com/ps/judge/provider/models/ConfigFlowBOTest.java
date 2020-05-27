package com.ps.judge.provider.models;

import com.ps.judge.dao.entity.ConfigFlowDO;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigFlowBOTest {

    @Test
    public void statusTest() {
        ConfigFlowBO.Status stopped = ConfigFlowBO.Status.STOPPED;
        ConfigFlowBO.Status stopped1 = ConfigFlowBO.Status.valueOf("STOPPED");
        ConfigFlowBO.Status stopped3 = ConfigFlowBO.Status.valueOf(0);

        ConfigFlowDO configFlowDO = new ConfigFlowDO();


        System.out.println(stopped.equals(stopped1));

    }

}