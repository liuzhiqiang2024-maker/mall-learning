package com.example.malldemo.auth;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class TokenStore {
    /** token → userId */
    private final Map<String,Long>tokenToUserId = new ConcurrentHashMap<>();
    /** 签发：为某个用户生成一个新 token */
    public String issue(Long userId){
        String token="token-"+UUID.randomUUID().toString().replaceAll("-","");
        tokenToUserId.put(token,userId);
        return token;
    }
    /** 校验：token 有效就返回 userId，无效返回 null */
    public Long resolve(String token){
        return tokenToUserId.get(token);
    }

}
