package com.springboot.template.business.sample.web;

import com.springboot.template.common.web.FilterConfig;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 해당 비즈니스 내 에서만 동작하는 필터
 * 아래 FilterConfig 에 추가
 * @see FilterConfig
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
public class SampleFilter implements Filter {

    /**
     * 애플리케이션 시작 시 실행
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        log.info("SampleFilter.init");
    }

    /**
     * 생성된 쓰레드 1개의 즉, 요청 1회당 실행
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("SampleFilter.doFilter start");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("SampleFilter.doFilter end");
    }

    /**
     * 애플리케이션 종료 시 실행, 비정상 종료면 실행 안됨
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void destroy() {
        //애플리케이션 종료 시
        log.info("SampleFilter.destroy");
    }
}
