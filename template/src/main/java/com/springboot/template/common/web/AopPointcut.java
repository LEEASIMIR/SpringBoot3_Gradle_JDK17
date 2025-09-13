package com.springboot.template.common.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.template.common.exception.ExceptionHandlerRestApi;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * API, @RestController 대상 모니터링
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Aspect
public class AopPointcut {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController(){}
    @Pointcut("execution(* com.springboot.template.business.sample.controller.api.*.*(..))")
    public void sampleApi(){}
}
