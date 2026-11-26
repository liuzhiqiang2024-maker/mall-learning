package com.example.malldemo.auth;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class TokenStore {
    // 在当前进程内保存 token 到用户 ID 的映射。ConcurrentHashMap 支持并发访问。
    // 当前没有过期清理，应用重启后这些映射会丢失。
    private final Map<String,Long>tokenToUserId = new ConcurrentHashMap<>();
    /** 每次登录生成一个随机 token，并关联到对应的用户 ID。 */
    public String issue(Long userId){
        String token="token-"+UUID.randomUUID().toString().replaceAll("-","");
        tokenToUserId.put(token,userId);
        return token;
    }
    /** 根据完整 token 查找用户 ID，查不到时返回 null。 */
    public Long resolve(String token){
        return tokenToUserId.get(token);
    }

}
