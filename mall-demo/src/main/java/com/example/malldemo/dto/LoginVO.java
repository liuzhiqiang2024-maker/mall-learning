package com.example.malldemo.dto;
import lombok.Data;
// 登录成功后的响应。token 由服务端签发，客户端后续通过 Authorization 请求头携带它。
@Data

public class LoginVO {
    private Long userId;
    private String username;
    private String token;
}
