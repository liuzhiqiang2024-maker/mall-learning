package com.example.malldemo.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
// 登录请求体携带用户输入的密码，Service 会用它与数据库中的密码哈希核对。
@Data
public class LoginDTO {
    @NotBlank(message="用户名不能为空")
    private String username;

    @NotBlank(message="密码不能为空")
    private String password;
}
