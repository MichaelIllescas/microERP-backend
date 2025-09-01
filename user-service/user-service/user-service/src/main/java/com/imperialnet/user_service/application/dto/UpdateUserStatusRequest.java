package com.imperialnet.user_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusRequest {
    private String status; // ACTIVE, INACTIVE, SUSPENDED, etc.
}
