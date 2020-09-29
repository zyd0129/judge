package com.ts.judge.provider.flow.node;

import com.ts.judge.provider.enums.RunTimeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点可以复用，即一个节点可以在多个process里定义，一个process也可以定义多个相同节点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeInstance {

    /**
     * start,bluRay,TxScore,Rule,Jury,end
     */
    private String nodeType;
    private String name;
    private RunTimeStatus status;
    private String msg;
    private String nodeInstanceId;
}
