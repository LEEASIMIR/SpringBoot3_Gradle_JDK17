package com.springboot.template.global.config.web.servlet.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalFilterConfig {
    @Bean
    public FilterRegistrationBean<GlobalFilter> globalUrlFilter() {
        FilterRegistrationBean<GlobalFilter> registrationBean = new FilterRegistrationBean<>();

        // 필터 클래스 지정
        registrationBean.setFilter(new GlobalFilter());

        // 특정 URL 패턴에만 적용
        registrationBean.addUrlPatterns("/**");

        // 필터 순서 지정
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
