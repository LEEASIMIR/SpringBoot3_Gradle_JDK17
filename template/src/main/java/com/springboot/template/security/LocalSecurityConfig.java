package com.springboot.template.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

/**
 * @see SecurityConfig
 * @author 이봉용
 * @date 25. 9. 14.
 */
@Profile("local")
@Configuration
public class LocalSecurityConfig implements SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**")
                .access(new WebExpressionAuthorizationManager(
                        "hasIpAddress('127.0.0.1/24') or hasIpAddress('0:0:0:0:0:0:0:1')"))
                .anyRequest().permitAll()
        );
        return http.build();
    }
}
