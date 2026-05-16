package com.woojin.nerdinary_taem_o.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NE-O-RDINARY Team O API")
                        .description("NE-O-RDINARY Team O backend API documentation")
                        .version("v1"));
    }
}
