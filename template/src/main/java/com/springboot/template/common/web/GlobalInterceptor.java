package com.springboot.template.common.web;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                                @Nonnull HttpServletResponse response,
                                @Nonnull Object handler) throws Exception {
        log.debug("GlobalInterceptor.preHandle");
        return true;
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request,
                                @Nonnull HttpServletResponse response,
                                @Nonnull Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("GlobalInterceptor.postHandle");
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request,
                                @Nonnull HttpServletResponse response,
                                @Nonnull Object handler, Exception ex) throws Exception {
        log.debug("GlobalInterceptor.afterCompletion");
    }
}
