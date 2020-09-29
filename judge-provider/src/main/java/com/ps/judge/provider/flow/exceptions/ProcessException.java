package com.ps.judge.provider.flow.exceptions;


import com.ps.common.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessException extends Exception {
    private int code;
    private String message;

    public ProcessException(BizException.BizExceptionEnum status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
