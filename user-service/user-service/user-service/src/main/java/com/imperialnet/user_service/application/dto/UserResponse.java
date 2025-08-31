package com.imperialnet.user_service.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String keycloakId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
