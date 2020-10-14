package com.ts.judge.provider.flow.script;

import org.springframework.stereotype.Service;

@Service
public class TextClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String s) throws ClassNotFoundException {
        byte[] data = s.getBytes();
        //找到class调用核心defineClass方法返回一个Class对象
        return defineClass(s, data, 0, data.length);
    }
}
