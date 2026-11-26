package com.example.malldemo.auth;
import com.example.malldemo.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component

public class AuthInterceptor implements HandlerInterceptor {
    /** 拦截器和 Controller 共用的请求属性名。 */
    public static final String ATTR_USER_ID = "currentUserId";

    @Autowired
    private TokenStore tokenStore;

    // Spring MVC 在调用 Controller 之前执行 preHandle，返回 true 表示继续处理请求。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 浏览器可用 OPTIONS 询问跨域规则，这类预检请求在此直接放行。
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        // 当前接口直接接收 Authorization 中的 token，并用完整的请求头值查找。
        String authorization = request.getHeader("Authorization");

        // 缺少凭证时抛出业务异常，由全局异常处理器生成响应。
        if (authorization == null || authorization.isBlank()) {
            throw new BusinessException(401, "未登录，请先登录");
        }

        Long userId = tokenStore.resolve(authorization);
        if (userId == null) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }

        // 该属性仅在本次请求内有效，Controller 通过 @RequestAttribute 取得登录用户 ID。
        request.setAttribute(ATTR_USER_ID, userId);
        return true;
    }

}
