package com.example.malldemo.service.impl;
import com.example.malldemo.auth.TokenStore;
import com.example.malldemo.dto.LoginDTO;
import com.example.malldemo.dto.LoginVO;
import com.example.malldemo.entity.User;
import com.example.malldemo.exception.BusinessException;
import com.example.malldemo.mapper.UserMapper;
import com.example.malldemo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class AuthServiceImpl  implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginVO login(LoginDTO dto) {
        //1.按照用户名查人
        User user=userMapper.findByUsername(dto.getUsername());
        //2.用户不存在或者密码不匹配（同一个错误提示语）
        if(user==null||!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new BusinessException(401,"用户名或密码错误");
        }
        //3.签发token
        String token= tokenStore.issue(user.getId());

        LoginVO vo=new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);
        return vo;
    }
}
