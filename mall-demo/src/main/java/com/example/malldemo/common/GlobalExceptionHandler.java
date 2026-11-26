package com.example.malldemo.common;
import com.example.malldemo.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
// 集中处理 MVC 请求中的异常，并使用 Result 返回统一的 JSON 结构。
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return Result.fail(405, "请求方法不支持：" + e.getMethod() + "，该接口只支持 " + e.getSupportedHttpMethods());
    }

    // 业务异常自带错误码和提示，例如未登录时的 401。
    @ExceptionHandler(BusinessException.class)
    public Result<?>handleBusinessException(BusinessException e){
        log.warn("业务异常：{}",e.getMessage());
        // Result.code 是响应体字段。这里没有设置 HTTP 状态，响应通常仍是 HTTP 200。
        return Result.fail(e.getCode(),e.getMessage());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return Result.fail(404, "接口不存在：" + e.getResourcePath());
    }
    // 未预期的异常记录到服务端日志，客户端收到通用错误提示。
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统异常",e);
        return Result.fail(500,"服务器内部错误");

    }
    // 收集方法参数约束产生的错误，例如查询参数上的 @Positive。
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e){
        List<String> errorMsgList=new ArrayList<>();
        Set<ConstraintViolation<?>> violations=e.getConstraintViolations();
        for(ConstraintViolation<?> item:violations){
            errorMsgList.add(item.getMessage());
        }
        return Result.fail(400,"参数校验失败",errorMsgList);
    }
    // 请求体已经转成 DTO，但字段未通过 @Valid 校验。此处保留字段名和错误信息。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errorsMsgList = new ArrayList<>();
        for(FieldError item:fieldErrors){
            String msg = item.getField() + "：" + item.getDefaultMessage();
            errorsMsgList.add(msg);
        }
        return Result.fail(400,"参数校验失败",errorsMsgList);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.fail(400,"参数类型错误，请检查传入数据类型",null);
    }


}
