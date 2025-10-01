package com.springboot.template.security;

import com.springboot.template.security.handler.LoginFailHandler;
import com.springboot.template.security.handler.LoginSuccessHandler;
import com.springboot.template.security.handler.LogoutSuccessHandler;
import com.springboot.template.security.token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

/**
 * @see SecurityConfig
 * @author 이봉용
 * @date 25. 9. 14.
 */
@Profile("local")
@RequiredArgsConstructor
@Configuration
public class LocalSecurityConfig implements SecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailHandler loginFailHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false);
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**")
                .access(new WebExpressionAuthorizationManager(
                        "hasIpAddress('127.0.0.1/24') or hasIpAddress('0:0:0:0:0:0:0:1')"))
                .anyRequest().permitAll()
        )
        .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지 경로
                .loginProcessingUrl("/perform_login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/perform_logout") // 로그아웃 처리 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll()
        );
        return http.build();
    }
}
