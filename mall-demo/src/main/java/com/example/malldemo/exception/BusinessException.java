package com.example.malldemo.exception;

// 库存不足等业务规则校验失败时抛出。GlobalExceptionHandler 用业务码和异常消息组装 Result。
public class BusinessException extends RuntimeException{
    private Integer code;
    public BusinessException(Integer code,String msg) {
        // 父类保存消息，异常处理器通过 getMessage() 读取。
        super(msg);
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }
}
