package com.springboot.template.global.config.web.spring.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * API, @RestController 대상 모니터링
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class GlobalAopRestApiMonitoring {

    private final ObjectMapper objectMapper;

    /**
     * @Around 전후 실행, 대상: @RestController 를 가지고 있는 모든 클래스
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
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
        Throwable throwable = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        long executionTime = System.currentTimeMillis() - start;

        if(!Objects.isNull(throwable)) {
            log.error("{}:{}[{}ms]", method, url, executionTime);
            log.error(throwable.getMessage(), throwable);
            throw throwable;
        } else {
            log.info("{}:{}[{}ms]", method, url, executionTime);
            return result;
        }
    }
}
