package com.springboot.template.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class Group {
    @Bean
    public GroupedOpenApi storeOpenApi() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder().group("api").pathsToMatch(paths)
                .build();
    }
}
