package com.ps.judge.provider.flow.variable;

import lombok.Data;

/**
 * {
 *       {
 *             "varName": "deviceIsAnEmulator",
 *             "varValue": "2",
 *             "varType": "String",
 *             "varGroup": "externalChannelData",
 *             "varHasError": false,
 *             "varErrorMsg": "",
 *             "varDefaultValue": "0"
 *         }
 */
@Data
public class ProcessVariable {
    private String varName;
    private String varValue;
    private String varType;
    private String varGroup;
    private Boolean varHasError;
    private String varErrorMsg;
    private String varDefaultValue;
}
