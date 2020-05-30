package com.ps.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 10102
 */
@Data
@Builder
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 7614396417801217389L;
    /**
     * 返回码 0：成功，其他为错误
     */
    private int code;
 
    /**
     * 错误消息
     */
    private String message;
 
    /**
     * 正确时返回的数据
     */
    private T data;
 
    private static final int SUCCESS = 0;
 
    public boolean isSuccess() {
        return code == 0;
    }
 
    public static ApiResponse error(int errorCode, String msg) {
        ApiResponse apiResponse =  ApiResponse
                .builder()
                .code(errorCode)
                .message(msg)
                .build();
        return apiResponse;
    }
 
    public static<T> ApiResponse<T> success(T data) {
        ApiResponse apiResponse =  ApiResponse
                .builder()
                .code(SUCCESS)
                .data(data)
                .build();
        return apiResponse;
    }
 
    public static ApiResponse success() {
        ApiResponse apiResponse =  ApiResponse
                .builder()
                .code(SUCCESS)
                .build();
        return apiResponse;
    }

}