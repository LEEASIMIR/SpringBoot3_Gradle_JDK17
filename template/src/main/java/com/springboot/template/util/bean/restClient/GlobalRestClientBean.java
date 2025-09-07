package com.springboot.template.util.bean.restClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * 외부 요청을 위한 빈 등록
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Configuration
public class GlobalRestClientBean {
    @Bean
    public RestClient defaultRestClient() {
        return RestClient.builder().baseUrl("http://localhost:8080").build();
    }
    @Bean
    public RestClient naverRestClient() {
        return RestClient.builder().baseUrl("https://www.naver.com").build();
    }
}
