package com.springboot.template.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 주요 필터별 역할<br/>
 * {@link org.springframework.security.web.session.DisableEncodeUrlFilter}: URL에 세션 ID가 노출되지 않도록 URL 인코딩을 비활성화합니다. 이는 보안에 취약한 URL Rewriting 방식 대신 쿠키 기반 세션 관리를 선호하는 현대적 웹 환경에서 주로 사용됩니다.<br/>
 * {@link org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter}: 비동기 요청(AsyncContext)을 처리할 때 SecurityContext가 올바르게 전달되도록 통합하는 역할을 합니다. 이를 통해 비동기 스레드에서도 인증 정보를 유지할 수 있습니다.<br/>
 * {@link org.springframework.security.web.context.SecurityContextHolderFilter}: SecurityContextHolder에 저장된 SecurityContext를 확인하여 현재 요청에 대한 인증 정보를 설정하거나, 요청 처리가 끝난 후 정리합니다. 이는 요청의 시작과 끝에서 보안 컨텍스트를 관리하는 중요한 역할을 합니다.<br/>
 * {@link org.springframework.security.web.header.HeaderWriterFilter}: Content-Security-Policy, X-Frame-Options 등과 같은 보안 관련 HTTP 헤더를 응답에 추가합니다. 이를 통해 클릭재킹(Clickjacking)이나 XSS(Cross-Site Scripting) 같은 공격을 방어합니다.<br/>
 * {@link org.springframework.security.web.csrf.CsrfFilter}: CSRF(Cross-Site Request Forgery) 공격을 방어하는 역할을 합니다. POST, PUT, DELETE와 같은 요청에 대해 CSRF 토큰의 유효성을 검사합니다.<br/>
 * {@link org.springframework.security.web.authentication.logout.LogoutFilter}: /logout과 같은 특정 URL에 대한 요청을 감지하여 사용자의 로그아웃을 처리합니다. 이 필터는 세션 무효화, 인증 정보 삭제 등의 작업을 수행합니다.<br/>
 * {@link org.springframework.security.web.savedrequest.RequestCacheAwareFilter}: 사용자가 인증되지 않은 상태에서 보호된 자원에 접근했을 때, 원래 요청 정보를 **캐시(Cache)**에 저장합니다. 인증 성공 후 사용자를 원래 요청했던 페이지로 리다이렉트하는 역할을 합니다.<br/>
 * {@link org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter}: HttpServletRequest를 SecurityContextHolderAwareRequestWrapper로 래핑하여, JSP나 서블릿에서 request.getUserPrincipal() 또는 request.isUserInRole()과 같은 메서드를 사용할 수 있도록 합니다.<br/>
 * {@link org.springframework.security.web.authentication.AnonymousAuthenticationFilter}: 인증되지 않은 익명 사용자에게도 **AnonymousAuthenticationToken**을 부여합니다. 이를 통해 익명 사용자가 접근 가능한 자원에 대해서도 Spring Security의 인가(Authorization) 처리가 가능하도록 합니다.<br/>
 * {@link org.springframework.security.web.access.ExceptionTranslationFilter}: 필터 체인에서 발생하는 인증(Authentication) 또는 인가(Authorization) 예외를 처리합니다. 예를 들어, AccessDeniedException이 발생하면 403 Forbidden 페이지로 리다이렉션하거나, AuthenticationException이 발생하면 로그인 페이지로 리다이렉션하는 역할을 담당합니다.<br/>
 * {@link org.springframework.security.web.access.intercept.AuthorizationFilter}: 필터 체인의 가장 마지막에 위치하며, 현재 사용자가 요청하는 URL에 접근할 수 있는 권한(Permission)이 있는지 검사합니다. 이 필터는 AccessDecisionManager를 사용하여 최종 인가 결정을 내립니다.<br/>
 * @author 이봉용
 * @date 25. 9. 14.
 */
public interface SecurityConfig {
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception;
}
