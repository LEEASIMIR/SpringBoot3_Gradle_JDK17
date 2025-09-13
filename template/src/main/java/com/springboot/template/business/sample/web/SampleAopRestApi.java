package com.springboot.template.business.sample.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 대상: sample.api 내 대상
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class SampleAopRestApi {

    @Before("com.springboot.template.common.web.AopPointcut.sampleApi()")
    public void beforeApi(JoinPoint joinPoint) {
        log.info("sampleApi Before");
    }
    @After("com.springboot.template.common.web.AopPointcut.sampleApi()")
    public void afterApi(JoinPoint joinPoint) {
        log.info("sampleApi After");
    }
}
