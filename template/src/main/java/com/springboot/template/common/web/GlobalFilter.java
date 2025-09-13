package com.springboot.template.common.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * 모든 요청을 대상으로 한 글로벌 필터
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Slf4j
public class GlobalFilter implements Filter {

    /**
     * 애플리케이션 시작 시 실행
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        log.info("GlobalFilter.init");
    }

    /**
     * 생성된 쓰레드 1개의 즉, 요청 1회당 실행
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("GlobalFilter.doFilter start");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        MDC.put("sessionId", request.getSession().getId());
        log.info(request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
        log.info("GlobalFilter.doFilter end");
    }

    /**
     * 애플리케이션 종료 시 실행, 비정상 종료면 실행 안됨
     * @author 이봉용
     * @date 25. 9. 7.
     */
    @Override
    public void destroy() {
        //애플리케이션 종료 시
        log.info("GlobalFilter.destroy");
    }
}
