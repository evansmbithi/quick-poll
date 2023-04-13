package com.apress;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration

public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.apress.Controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        // title, description, contact, license information 
        return new ApiInfo(
            "QuickPoll REST API",
            "QuickPoll Api for creating and managing polls",
            "http://example.com/terms-of-service",
            "Terms of Service",
            new Contact("Evans Mbithi", "www.mbithi.rocks", "hello@mbithi.rocks"),
            "MIT License", "http://opensource.org/licenses/MIT", Collections.emptyList());
    }
    
}
