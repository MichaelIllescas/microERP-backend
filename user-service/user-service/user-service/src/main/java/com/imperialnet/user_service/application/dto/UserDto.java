package com.imperialnet.user_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;            // ID local (DB propia)
    private String keycloakId;  // ID de Keycloak
    private String name;
    private String email;
    private String phone;
}
