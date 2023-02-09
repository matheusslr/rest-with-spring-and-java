package br.com.matheusslr.restwithspringandjava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("RESTful API with Java 17 and Spring Boot 3")
                        .version("v1")
                        .description("Repository focused on studies of a RESTful API with Spring Boot 3," +
                                " unit tests and integration with JUnit 5 and Mockito," +
                                " authentication with JWT and Spring Security and API integration in AWS.")
                        .termsOfService("https://github.com/matheusslr/rest-with-spring-and-java")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/matheusslr/rest-with-spring-and-java")));
    }

}
