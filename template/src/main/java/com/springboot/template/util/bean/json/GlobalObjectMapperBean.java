package com.springboot.template.util.bean.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JSON 조작을 위한 빈 등록
 * @author 이봉용
 * @date 25. 9. 7.
 */
@Configuration
public class GlobalObjectMapperBean {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
