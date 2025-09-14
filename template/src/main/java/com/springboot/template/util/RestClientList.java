package com.springboot.template.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientList {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public RestClient localhostRestClient() {
        return RestClient.builder().baseUrl("http://localhost:"+serverPort).build();
    }
    @Bean
    public RestClient naverRestClient() {
        return RestClient.builder().baseUrl("https://www.naver.com").build();
    }
}
