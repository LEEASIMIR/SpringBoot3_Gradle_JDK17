package com.springboot.template.util.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanUtil {
    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
    @Bean
    public RestClient defaultRestClient() {
        return RestClient.builder().baseUrl("http://localhost:8080").build();
    }
    @Bean
    public RestClient naverRestClient() {
        return RestClient.builder().baseUrl("https://www.naver.com").build();
    }
}
