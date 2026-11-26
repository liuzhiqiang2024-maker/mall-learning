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
        // 调用 UserMapper.xml 中的查询，读取用户信息和数据库里的密码哈希。
        User user=userMapper.findByUsername(dto.getUsername());
        // BCrypt 的 matches 用提交的密码和已存的哈希做校验。
        // 用户不存在时，|| 会短路，跳过密码校验。两种失败使用相同提示。
        if(user==null||!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new BusinessException(401,"用户名或密码错误");
        }
        // 校验通过后，TokenStore 生成随机 token 并记录它对应的用户。
        String token= tokenStore.issue(user.getId());

        // LoginVO 只公开登录接口需要返回的字段，密码哈希留在服务端。
        LoginVO vo=new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setToken(token);
        return vo;
    }
}
