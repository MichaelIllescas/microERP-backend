package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UpdateUserStatusRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.UpdateUserStatusUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserStatusService implements UpdateUserStatusUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final KeycloakUserPort keycloakUserPort;
    private final UserMapper mapper;

    @Override
    public UserResponse updateStatus(Long id, UpdateUserStatusRequest request) {
        log.info("🔄 Solicitando actualización de estado para userId={} -> nuevoEstado={}", id, request.getStatus());

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado en BD con id={}", id);
                    return new RuntimeException("User not found");
                });

        // 1) actualizar en Keycloak
        try {
            keycloakUserPort.updateStatus(user.getKeycloakId(), request.getStatus());
            log.info("✅ Estado actualizado en Keycloak para keycloakId={} -> {}", user.getKeycloakId(), request.getStatus());
        } catch (Exception ex) {
            log.error("❌ Error actualizando estado en Keycloak para keycloakId={}", user.getKeycloakId(), ex);
            throw ex;
        }

        // 2) actualizar en dominio/local DB
        user.setStatus(request.getStatus());
        User updated = userRepositoryPort.save(user);
        log.info("💾 Estado actualizado en BD local para userId={} -> {}", updated.getId(), updated.getStatus());

        UserResponse response = mapper.toResponse(updated);
        log.info("🎯 Operación completada: userId={} estadoFinal={}", response.getId(), response.getStatus());

        return response;
    }
}
