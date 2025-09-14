package com.springboot.template.common.web;

import com.springboot.template.business.sample.web.SampleFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 모든 필터 설정
 * @author 이봉용
 * @date 25. 9. 11.
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<GlobalFilter> globalFilter() {
        FilterRegistrationBean<GlobalFilter> registrationBean = new FilterRegistrationBean<>();

        // 필터 클래스 지정
        registrationBean.setFilter(new GlobalFilter());

        // 특정 URL 패턴에만 적용
        registrationBean.addUrlPatterns("/*");

        // 필터 순서 지정
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SampleFilter> sampleFilter() {
        FilterRegistrationBean<SampleFilter> registrationBean = new FilterRegistrationBean<>();

        // 필터 클래스 지정
        registrationBean.setFilter(new SampleFilter());

        // 특정 URL 패턴에만 적용
        registrationBean.addUrlPatterns("/sample/*");

        // 필터 순서 지정
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
