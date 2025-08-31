package com.imperialnet.user_service.domain.model;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    private Long id;             // id local en la BD
    private String keycloakId;   // id en Keycloak
    private String username;     // único
    private String firstName;
    private String lastName;
    private String email;        // único
    private String phone;
}
