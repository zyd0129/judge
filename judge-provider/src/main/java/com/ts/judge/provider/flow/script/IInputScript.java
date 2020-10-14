package com.ts.judge.provider.flow.script;

import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.jury.api.request.ApplyRequest;

import java.util.Map;

public interface IInputScript {
    Map<String, Object> parse(ProcessInstance processInstance);
}
