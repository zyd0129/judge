package com.ts.judge.provider.enums;

import org.junit.Test;

import static org.junit.Assert.*;

public class RiskCodeEnumTest {

    @Test
    public void test() {
        RiskCodeEnum reject = RiskCodeEnum.valueOf("REJECT");
        System.out.println(reject);
    }

}