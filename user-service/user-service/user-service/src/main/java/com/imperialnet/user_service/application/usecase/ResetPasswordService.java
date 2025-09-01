package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
import com.imperialnet.user_service.application.port.in.ResetPasswordUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCase {

    private final KeycloakUserPort keycloakUserPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void sendResetPasswordEmail(String userId) {
        log.warn("⚠️ Solicitud de reseteo de contraseña para userId={}", userId);

        Long userIdL;
        try {
            userIdL = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            log.error("❌ userId={} no es un número válido", userId, ex);
            throw new IllegalArgumentException("El ID de usuario debe ser numérico");
        }

        User user = userRepositoryPort.findById(userIdL)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado en BD con id={}", userIdL);
                    return new ResourceNotFoundException("Usuario no encontrado id=" + userIdL);
                });

        try {
            keycloakUserPort.sendResetPasswordEmail(user.getKeycloakId());
            log.info("✅ Email de reseteo enviado a usuario id={} keycloakId={}", user.getId(), user.getKeycloakId());
        } catch (Exception ex) {
            log.error("❌ Error enviando email de reseteo a Keycloak para userId={} keycloakId={}",
                    user.getId(), user.getKeycloakId(), ex);
            throw ex;
        }
    }
}
