package com.example.malldemo.common;
import com.example.malldemo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 捕获我们自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?>handleBusinessException(BusinessException e){
        log.warn("业务异常：{}",e.getMessage());
        return Result.fail(500,e.getMessage());
    }
    // 兜底捕获所有其他异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常",e);
        return Result.fail(500,"服务器内部错误");

    }
}
