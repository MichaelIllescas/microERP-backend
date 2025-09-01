package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.ChangePasswordRequest;
import com.imperialnet.user_service.application.port.in.ChangePasswordUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final KeycloakUserPort keycloakUserPort;

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        log.info("🔐 Iniciando cambio de contraseña para userId={}", userId);

        // Buscar usuario en BD local
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado en BD local con id={}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });

        log.debug("Usuario encontrado: id={} email={} keycloakId={}",
                user.getId(), user.getEmail(), user.getKeycloakId());

        // Llamar a Keycloak para cambiar password
        try {
            keycloakUserPort.updatePassword(user.getKeycloakId(), request.getNewPassword());
            log.info("✅ Contraseña actualizada en Keycloak para userId={} (keycloakId={})",
                    userId, user.getKeycloakId());
        } catch (Exception ex) {
            log.error("❌ Error actualizando contraseña en Keycloak para userId={} (keycloakId={})",
                    userId, user.getKeycloakId(), ex);
            throw ex;
        }
    }
}
