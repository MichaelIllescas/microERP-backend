package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.GetCurrentUserUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of GetCurrentUserUseCase.
 */
@Service
public class GetCurrentUserService implements GetCurrentUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;

    public GetCurrentUserService(UserRepositoryPort userRepositoryPort, UserMapper userMapper) {
        this.userRepositoryPort = userRepositoryPort;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getCurrentUser(String keycloakId) {
        User user = userRepositoryPort
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found with Keycloak ID: " + keycloakId));
        return userMapper.toResponse(user);
    }
}
