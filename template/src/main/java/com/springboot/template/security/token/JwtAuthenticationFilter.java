package com.springboot.template.security.token;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
                                    @Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header 에서 토큰을 꺼냄
        String token = parseBearerToken(request);

        // 2. 토큰이 존재하고 유효한지 검사 (validateToken)
        //    토큰이 유효하면 인증 객체(Authentication)를 생성.
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // SecurityContextHolder: 인증 객체를 담고 있는 컨테이너
            // SecurityContext: 인증 객체를 담고 있음
            // Authentication: 인증 정보 (누가, 어떤 권한을 가졌는지)
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext 에 인증 정보를 저장.
            // 요청이 끝날 때까지 해당 쓰레드에서 인증 정보가 유지됨.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 제어를 넘김
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // "Authorization" 헤더에서 "Bearer " 접두사를 가진 토큰을 찾음
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }
}
