package com.example.malldemo.common;
import lombok.Data;
// 统一接口响应。code 是响应体里的业务码，T 是 data 的类型，例如 LoginVO 或 PageResult<Orders>。
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    // 部分失败响应需要附带数据，例如请求参数校验失败时的错误列表。
    public static <T> Result<T> fail(Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}

