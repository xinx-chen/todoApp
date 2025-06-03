package com.example.todolistmybatis.interceptor;

import com.example.todolistmybatis.common.ErrorCode;
import com.example.todolistmybatis.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    TokenUtil tokenUtil;
    @Value("${token.refreshTime}")
    private Long refreshTime;
    @Value("${token.expiresTime}")
    private Long expiresTime;
    /**
     * 权限认证的拦截操作.
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //为空就返回错误
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        if (null == token || "".equals(token.trim())) {
            return false;
        }
        Map<String, String> map = tokenUtil.parseToken(token);
        String userId = map.get("userId");
        long timeOfUse = System.currentTimeMillis() - Long.parseLong(map.get("timeStamp"));
        //1.判断 token 是否过期
        if (timeOfUse < refreshTime) {
            return true;
        }
        //超过token刷新时间，刷新 token
        else if (timeOfUse >= refreshTime && timeOfUse < expiresTime) {
            httpServletResponse.setHeader("token",tokenUtil.getToken(userId));
            return true;
        }
        //token过期就返回 token 无效.
        else {
            sendErrorResponse(httpServletResponse, ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage());
            return false;
        }
    }
    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        response.getWriter().write(
                String.format("{\"code\": %d, \"message\": \"%s\", \"success\": false}", code, message)
        );
}
}