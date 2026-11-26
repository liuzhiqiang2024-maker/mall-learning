package com.example.malldemo.entity;
import lombok.Data;
import java.time.LocalDateTime;
// 对应 users 表，由 UserMapper 在登录时按用户名读取。
@Data
public class User {
    private Long id;
    private String username;
    // 这里映射数据库中的密码哈希，登录响应使用 LoginVO。
    private String password;
    private LocalDateTime createTime;
}
