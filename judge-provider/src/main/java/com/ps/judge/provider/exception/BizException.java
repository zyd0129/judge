package com.ps.judge.provider.exception;

public class BizException extends RuntimeException {
    private int code;
    private String message;

    public BizException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(BizExceptionEnum status) {
        this.code=status.getCode();
        this.message=status.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum BizExceptionEnum {
        CONTAINER_START_FAILURE(1001, "flow start failure");
        // 成员变量
        private int code;
        private String message;


        // 构造方法
        private BizExceptionEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
