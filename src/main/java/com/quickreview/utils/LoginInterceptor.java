package com.quickReview.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.see if we need to intercept, based on session
        if (UserHolder.getUser() == null) {
            // no, then intercept and return 401
            response.setStatus(401);
            // 拦截
            return false;
        }
        // yes, then let it pass
        return true;
    }
}
