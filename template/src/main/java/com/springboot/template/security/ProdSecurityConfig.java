package com.springboot.template.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

/**
 * @see SecurityConfig
 * @author 이봉용
 * @date 25. 9. 14.
 */
@Profile("prod")
@Configuration
public class ProdSecurityConfig implements SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**")
                .access(new WebExpressionAuthorizationManager(
                        "hasIpAddress('127.0.0.1/24') or hasIpAddress('0:0:0:0:0:0:0:1')"))
                .anyRequest().permitAll()
        );
        return http.build();
    }
}
