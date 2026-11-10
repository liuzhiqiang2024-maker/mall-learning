package com.example.malldemo.common;
import lombok.Data;
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    //成功
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    //失败
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    public static <T> Result<T> fail(Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}

