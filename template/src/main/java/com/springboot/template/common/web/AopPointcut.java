package com.springboot.template.common.web;

import org.aspectj.lang.annotation.*;

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
