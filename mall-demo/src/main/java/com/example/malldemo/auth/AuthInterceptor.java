package com.example.malldemo.auth;
import com.example.malldemo.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component

public class AuthInterceptor implements HandlerInterceptor {
    /**
     * request 属性的 key：拦截器往里塞，Controller 从这里取
     */
    public static final String ATTR_USER_ID = "currentUserId";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // CORS 预检请求（浏览器跨域前会先发 OPTIONS），没有 token，必须放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        // 1. 从 Header 取凭证
        String authorization = request.getHeader("Authorization");

        // 2. 没带 → 拒绝
        if (authorization == null || authorization.isBlank()) {
            throw new BusinessException(401, "未登录，请先登录");
        }

        // 3. 带了但无效 → 拒绝
        Long userId = tokenStore.resolve(authorization);
        if (userId == null) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }

        // 4. 有效 → 把 userId 塞进请求，放行
        request.setAttribute(ATTR_USER_ID, userId);
        return true;
    }

}
