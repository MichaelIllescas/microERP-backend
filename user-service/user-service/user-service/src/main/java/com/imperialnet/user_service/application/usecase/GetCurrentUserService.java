package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.GetCurrentUserUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of GetCurrentUserUseCase.
 */
@Slf4j
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
        log.info("📥 Solicitando perfil del usuario con keycloakId={}", keycloakId);

        User user = userRepositoryPort
                .findByKeycloakId(keycloakId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado con keycloakId={}", keycloakId);
                    return new RuntimeException("User not found with Keycloak ID: " + keycloakId);
                });

        log.info("✅ Usuario encontrado: id={} email={}", user.getId(), user.getEmail());
        return userMapper.toResponse(user);
    }
}
