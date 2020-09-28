package com.ps.judge.provider.flow;

import com.ps.judge.provider.flow.node.NodeInstance;
import com.ps.judge.provider.service.FlowInstanceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class FlowInstance {

    @Autowired
    FlowInstanceService flowInstanceService;

    private Integer id;
    private String flowId;
    private List<NodeInstance> nodeList;
    private Integer currentIndex = -1;
    private NodeInstance currentNodeInstance;
    private String msg;

    /**
     * 这里才有透传机制，即所有参数传递给下一个节点
     */
    private Map<String, Object> flowParams;

    private RunTimeStatus status;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtCompleted;

    public void execute(Map<String, Object> params) {
        /**
         *  更新 flowInstance
         *  1. 设置next
         *  2.更新params
         *  3.更新状态时间
         */
        Integer nextIndex = next();
        if (nextIndex >= nodeList.size()) {
            return;
        }
        setCurrentIndex(nextIndex);
        currentNodeInstance = nodeList.get(nextIndex);
        currentNodeInstance.setStatus(NodeStatusEnums.RUNNING);
        flowParams = params;
        status = RunTimeStatus.RUNNING;
        gmtModified = LocalDateTime.now();
        flowInstanceService.update(this);

        currentNodeInstance.execute(flowParams, this);
    }

    private Integer next() {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= nodeList.size()) {
            nextIndex = nodeList.size() - 1;
        }
        return nextIndex;
    }
}
