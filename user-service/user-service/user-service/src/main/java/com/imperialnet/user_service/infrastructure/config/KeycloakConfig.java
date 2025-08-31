package com.imperialnet.user_service.infrastructure.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8082/")
                .realm("microerp")                  // realm de tus usuarios
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("user-service")           // el client que creaste
                .clientSecret("aZQEpMrz3ntpknsrpReDqvYoBfoDWVHg") // se genera al crear el client
                .build();
    }

}
