package com.ts.judge.provider.flow;

import com.alibaba.fastjson.JSONObject;
import com.ts.clerk.common.exception.BizException;
import com.ts.judge.api.object.ProcessResult;
import com.ts.judge.provider.enums.RunTimeStatus;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.service.impl.NodeInstanceService;
import com.ts.judge.provider.service.ProcessDefinitionService;
import com.ts.judge.provider.service.ProcessInstanceService;
import com.ts.jury.api.request.ApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 */
@Service
@Slf4j
public class ProcessEngine {

    @Autowired
    ProcessDefinitionService processDefinitionService;

    @Autowired
    ProcessInstanceService processInstanceService;

    @Autowired
    NodeInstanceService nodeInstanceService;

    /**
     *
     * @param processCode
     * @param applyRequest 这里对应的是工作流表单参数，需要转换成流程变量；目前由Jury节点完成；如果完全自动化配置，需要节点可配，完成出入参管理，表单、流程变量、节点数据之间的转换，表单是可以自动生成的
     * @return
     */

    public ProcessResult execute(String processCode, ApplyRequest applyRequest) {
        //1.获取工作流
        ProcessDefinition processDefinition = processDefinitionService.getByCode(processCode);
        if (processDefinition == null) {
            throw new BizException(70001, "工作流不存在");
        }

        //2,创建 processInstance,保存运行时数据

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setDefinitionId(processDefinition.getId());
        processInstance.setDefinition(processDefinition.getDefinition());
        processInstance.setCurrentNodeInstance(null);
        processInstance.setApplyRequest(applyRequest);
        processInstance.setStatus(RunTimeStatus.CREATED);
        processInstanceService.insert(processInstance);

        //3.执行流,异步执行 todo:自定义线程池
        processInstanceService.execute(processInstance);

        ProcessResult processResult = new ProcessResult();
        processResult.setProcessInstanceId(processInstance.getId());
        return processResult;
    }

    /**
     * 这里用于异步回调节点，目前由流处理节点的回调；目前没有节点任务记录，后续可以设计让任务节点直接接收
     *
     * @param processInstanceId
     * @param params
     */
    @Async
    public void callback(Integer processInstanceId, Map<String, Object> params) {
        ProcessInstance flowInstance = processInstanceService.getById(processInstanceId);
        NodeInstance currentNodeInstance = flowInstance.getCurrentNodeInstance();
        if (!currentNodeInstance.getStatus().equals(RunTimeStatus.WAITING)) {
            log.info("{}疑似收到重复接口回调, params: {}", processInstanceId, JSONObject.toJSON(params));
            return;
        }
        nodeInstanceService.callback(currentNodeInstance,flowInstance,params);
    }
}
