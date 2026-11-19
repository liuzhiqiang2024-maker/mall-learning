package com.example.malldemo.service;
import com.example.malldemo.dto.LoginDTO;
import com.example.malldemo.dto.LoginVO;
public interface AuthService {
    LoginVO login(LoginDTO dto);
}
