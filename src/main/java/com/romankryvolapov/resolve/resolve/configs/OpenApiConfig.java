package com.romankryvolapov.resolve.resolve.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Task Management Service")
                        .version("v1")
                        .description("REST API для управления пользователями и задачами с зависимостями"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("tasks")
                .pathsToMatch("/tasks/**", "/users/**")
                .build();
    }
}
