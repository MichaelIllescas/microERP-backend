package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UpdateUserStatusRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.UpdateUserStatusUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UpdateUserStatusService implements UpdateUserStatusUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final KeycloakUserPort keycloakUserPort; // 👈 nuevo
    private final UserMapper mapper;

    @Override
    public UserResponse updateStatus(Long id, UpdateUserStatusRequest request) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1) actualizar en Keycloak
        keycloakUserPort.updateStatus(user.getKeycloakId(), request.getStatus());

        // 2) actualizar en dominio/local DB
        user.setStatus(request.getStatus());
        User updated = userRepositoryPort.save(user);

        return mapper.toResponse(updated);
    }
}

