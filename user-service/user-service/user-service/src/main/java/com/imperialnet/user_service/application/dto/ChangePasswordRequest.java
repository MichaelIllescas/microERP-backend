package com.imperialnet.user_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // Necesario para deserialización JSON
@AllArgsConstructor
public class ChangePasswordRequest {
    private String newPassword;
}
