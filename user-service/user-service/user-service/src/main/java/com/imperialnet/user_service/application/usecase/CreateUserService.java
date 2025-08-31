package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.CreateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.CreateUserUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final KeycloakUserPort keycloakUserPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // 1) Validaciones simples
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // 2) Convertir DTO -> Dominio
        User user = userMapper.toDomain(request);

        // 3) Crear en Keycloak
        String keycloakId = keycloakUserPort.createUser(user, request.getPassword());
        user.setKeycloakId(keycloakId);

        // 4) Guardar en BD local
        User saved = userRepositoryPort.save(user);

        // 5) Devolver como Response
        return userMapper.toResponse(saved);
    }
}
