package com.springboot.template.common.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.template.common.custom.exception.ExceptionHandlerRestApi;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * API, @RestController 대상 단순 모니터링
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Order(1)//마지막 껍질
@Component
public class AopRestApiMonitoring {

    private final ObjectMapper objectMapper;

    @Before("com.springboot.template.common.web.AopPointcut.restController()")
    public void beforeApi(JoinPoint joinPoint) throws Throwable {
        log.debug("AopRestApiMonitoring.beforeApi()");
    }

    @After("com.springboot.template.common.web.AopPointcut.restController()")
    public void afterApi(JoinPoint joinPoint) {
        log.debug("AopRestApiMonitoring.afterApi()");
    }

    @Around("com.springboot.template.common.web.AopPointcut.restController()")
    public Object aroundApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 1. 요청 URL 및 HTTP 메서드 가져오기
        String url = request.getRequestURI();
        String method = request.getMethod();

        log.info("{}:{}[-ms]", method, url);

        try {
            String params = objectMapper.writeValueAsString(request.getParameterMap());
            log.info("{}:{}:{}", method, url, params);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        Object result = null;
        long executionTime = 0;
        /**
         * ExceptionHandlerRestApi 에서 에러 처리
         * @see ExceptionHandlerRestApi
         * @author 이봉용
         * @date 25. 9. 13.
         */
        try {
            joinPoint.getSignature().getName();
            result = joinPoint.proceed();
        } finally {
            executionTime = System.currentTimeMillis() - start;
            log.info("{}:{}[{}ms]", method, url, executionTime);
        }

        return result;
    }
}
