package com.ts.judge.provider.flow.script;

import java.util.Map;

public interface IOutputScript {
    Map<String, Object> process(String body);
}
