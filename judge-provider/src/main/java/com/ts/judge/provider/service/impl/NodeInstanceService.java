package com.ts.judge.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ts.judge.provider.flow.ProcessInstance;
import com.ts.judge.provider.enums.RunTimeStatus;
import com.ts.judge.provider.flow.node.AsyncNode;
import com.ts.judge.provider.flow.node.Node;
import com.ts.judge.provider.flow.node.NodeInstance;
import com.ts.judge.provider.flow.node.NodeResult;
import com.ts.judge.provider.flow.node.type.EndNode;
import com.ts.judge.provider.service.ProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class NodeInstanceService {
    @Autowired
    Map<String, Node> nodeMap;

    @Autowired
    ProcessInstanceService processInstanceService;

    /**
     * 这里nodeInstance即 currentInstance,这里也可不作为参数
     *
     * @param nodeInstance
     * @param flowInstance
     */
    public void execute(NodeInstance nodeInstance, ProcessInstance flowInstance) {
        Node node = nodeMap.getOrDefault(nodeInstance.getNodeType(), null);
        if (node == null) {
            nodeInstance.setMsg("不支持的节点类型");
            nodeInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            processInstanceService.update(flowInstance);
            return;
        }
        log.info("processInstance-{}开始执行,节点{}-{}-{}", flowInstance.getId(), nodeInstance.getName(), nodeInstance.getNodeType(), nodeInstance.getNodeInstanceId());
        NodeResult nodeResult = node.execute(flowInstance,nodeInstance);
        log.info("processInstance-{}完成执行,节点{}-{}-{},结果{}", flowInstance.getId(), nodeInstance.getName(), nodeInstance.getNodeType(), nodeInstance.getNodeInstanceId(), JSONObject.toJSONString(nodeResult));
        if (!nodeResult.isSuccess()) {
            nodeInstance.setMsg(nodeResult.getMsg());
            nodeInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            processInstanceService.update(flowInstance);
            return;
        }

        if (!node.isSync()) {
            nodeInstance.setStatus(RunTimeStatus.WAITING);
            flowInstance.setStatus(RunTimeStatus.WAITING);
            processInstanceService.update(flowInstance);
            return;
        }
        //目前保留吧，数据库更新null值不好更新，需要增加一个专门的更新接口
        if (node instanceof EndNode) {
            nodeInstance.setStatus(RunTimeStatus.COMPLETED);
            flowInstance.setStatus(RunTimeStatus.COMPLETED);
            flowInstance.setCompletedTime(LocalDateTime.now());
            processInstanceService.update(flowInstance);
            return;
        }
        /**
         * 执行下一个节点
         */
        nodeInstance.setStatus(RunTimeStatus.COMPLETED);
        flowInstance.setStatus(RunTimeStatus.RUNNING);
        processInstanceService.update(flowInstance);
        processInstanceService.execute(flowInstance);
    }

    public void callback(NodeInstance nodeInstance, ProcessInstance flowInstance, Map<String, Object> params) {
        //这里目前只有bluRay
        Node node = nodeMap.getOrDefault(nodeInstance.getNodeType(), null);
        if (node == null) {
            nodeInstance.setMsg("不支持的节点类型");
            nodeInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            processInstanceService.update(flowInstance);
            return;
        }
        AsyncNode asyncNode = (AsyncNode) node;
        NodeResult nodeResult = asyncNode.callback(flowInstance, params);
        if (!nodeResult.isSuccess()) {
            nodeInstance.setMsg(nodeResult.getMsg());
            nodeInstance.setStatus(RunTimeStatus.EXCEPTION);
            flowInstance.setStatus(RunTimeStatus.EXCEPTION);
            processInstanceService.update(flowInstance);
            return;
        }
        processInstanceService.execute(flowInstance);
    }
}
