package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.CreateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.CreateUserUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final KeycloakUserPort keycloakUserPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("👤 Iniciando creación de usuario con email={}", request.getEmail());

        // 1) Validaciones simples
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            log.warn("⚠️ Intento de creación sin email");
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            log.warn("⚠️ Intento de creación sin password (email={})", request.getEmail());
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // 2) Convertir DTO -> Dominio
        User user = userMapper.toDomain(request);
        user.setStatus("ACTIVE");
        log.debug("Usuario mapeado desde DTO: username={} email={}", user.getUsername(), user.getEmail());

        // 3) Crear en Keycloak
        try {
            String keycloakId = keycloakUserPort.createUser(user, request.getPassword());
            user.setKeycloakId(keycloakId);
            log.info("✅ Usuario creado en Keycloak con keycloakId={}", keycloakId);
        } catch (Exception ex) {
            log.error("❌ Error creando usuario en Keycloak (email={})", request.getEmail(), ex);
            throw ex;
        }

        // 4) Guardar en BD local
        User saved = userRepositoryPort.save(user);
        log.info("💾 Usuario persistido en BD local con id={}", saved.getId());

        // 5) Devolver como Response
        UserResponse response = userMapper.toResponse(saved);
        log.info("🎯 Usuario creado exitosamente: id={} email={}", response.getId(), response.getEmail());

        return response;
    }
}
