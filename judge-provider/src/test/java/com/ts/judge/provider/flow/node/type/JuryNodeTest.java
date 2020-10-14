package com.ts.judge.provider.flow.node.type;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class JuryNodeTest {

    @Test
    public void test() {
        ArrayList<String> a = new ArrayList<>();
        a.add("hello");
        String s = JSONObject.toJSONString(a);
        System.out.println(s);
        Object ss = JSONObject.parse(s);
        String name = ss.getClass().getName();
        System.out.println(name);

    }

}