package com.imperialnet.customer_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customers Service API - MicroERP")
                        .description("Documentación de la API para la gestión de clientes en MicroERP")
                        .version("1.0.0"));
    }
}