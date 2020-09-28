package com.ps.judge.provider.flow;

import com.ps.judge.provider.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 *
 */
public class FlowEngine {

    @Autowired
    FlowService flowService;

    FlowResult execute(String flowCode, Map<String, Object> params) {
        //1.获取工作流
        Flow flow = new Flow();

        //2,创建 flowInstance,保存运行时数据

        FlowInstance flowInstance = new FlowInstance();
        flowInstance.setStatus(RunTimeStatus.CREATED);

        //3.执行流,异步执行
        flowInstance.execute(params);

        FlowResult flowResult = new FlowResult();
        flowResult.setFlowInstanceId(flowInstance.getId());
        return flowResult;
    }
}
