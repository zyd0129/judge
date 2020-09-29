package com.ts.judge.provider.exceptions;


import com.ts.clerk.common.exception.BizEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessException extends Exception {
    private int code;
    private String message;

    public ProcessException(BizEnum status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
