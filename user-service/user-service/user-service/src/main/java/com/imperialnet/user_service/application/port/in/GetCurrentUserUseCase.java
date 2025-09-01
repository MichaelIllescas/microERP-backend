package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.UserResponse;

/**
 * Use case for fetching the profile of the currently authenticated user.
 */
public interface GetCurrentUserUseCase {
    UserResponse getCurrentUser(String keycloakId);
}