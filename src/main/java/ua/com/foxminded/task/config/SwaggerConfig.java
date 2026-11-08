package ua.com.foxminded.task.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("University Spring Boot REST API")
                .description("University Management REST API")
                .version("0.0.1-SNAPSHOT")
                .contact(new Contact()
                    .name("Afanasiev Mykola")
                    .url("www.any.com")
                    .email("afniko@gmail.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
            .group("api")
            .pathsToMatch("/api/**")
            .packagesToScan("ua.com.foxminded.task.controller")
            .build();
    }
}
