package com.imperialnet.user_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String keycloakId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String status;
}
