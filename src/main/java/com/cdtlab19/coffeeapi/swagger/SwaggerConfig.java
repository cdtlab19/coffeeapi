package com.cdtlab19.coffeeapi.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cdtlab19.coffeeapi"))
                .paths(regex("/api.*"))
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API REST",
                "This API is will facilitate the communication between JAVA SDK and Blockchain",
                "1.0",
                "Terms of service",
                new Contact("Ktatal", "www.conductor.com.br", "fernando.souza@conductor.com.br"),
                "License of API", "/www.apache.org/licensen.html", Collections.emptyList());
    }
}
