package com.ts.judge.provider.flow.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScriptCacheTest {

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        String scriptText = "package com.ts.judge.provider.flow.script;class Demo{\n" +
                "    public void test() {\n" +
                "        List<String> a = new ArrayList();\n" +
                "        a.add(\"hello\");\n" +
                "        for (String s : a) {\n" +
                "            System.out.println(s);\n" +
                "        }\n" +
                "    }\n" +
                "}";
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class aClass = groovyClassLoader.parseClass(scriptText);
        GroovyObject o = (GroovyObject) aClass.newInstance();
        o.invokeMethod("test", null);

    }
}