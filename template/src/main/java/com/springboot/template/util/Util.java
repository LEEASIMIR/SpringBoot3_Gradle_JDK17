package com.springboot.template.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Util {
    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}
