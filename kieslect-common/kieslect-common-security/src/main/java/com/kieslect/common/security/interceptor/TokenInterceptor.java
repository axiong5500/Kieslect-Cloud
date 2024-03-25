package com.kieslect.common.security.interceptor;

import com.kieslect.common.core.constant.SecurityConstants;
import com.kieslect.common.core.context.TokenHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

public class TokenInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        TokenHolder.setToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TokenHolder.clear();
    }
}
