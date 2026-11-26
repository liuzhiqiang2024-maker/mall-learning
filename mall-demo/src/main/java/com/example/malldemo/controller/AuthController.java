package com.example.malldemo.controller;
import com.example.malldemo.common.Result;
import com.example.malldemo.dto.LoginDTO;
import com.example.malldemo.dto.LoginVO;
import com.example.malldemo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;

    // @RequestBody 将 JSON 转成 LoginDTO，@Valid 按 DTO 上的规则校验字段。
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
}
