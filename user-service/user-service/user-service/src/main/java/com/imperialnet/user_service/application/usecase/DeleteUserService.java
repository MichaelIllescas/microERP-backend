package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
import com.imperialnet.user_service.application.port.in.DeleteUserUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserService implements DeleteUserUseCase {

    private final UserRepositoryPort userRepository;
    private final KeycloakUserPort keycloakPort;

    @Override
    @Transactional
    public void execute(Long userId) {
        log.warn("⚠️ Solicitud de eliminación de usuario id={}", userId);

        // 1) Buscar usuario en DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("❌ Usuario no encontrado en BD con id={}", userId);
                    return new ResourceNotFoundException("Usuario no encontrado id=" + userId);
                });

        log.info("📥 Usuario encontrado en BD: id={} email={} keycloakId={}",
                user.getId(), user.getEmail(), user.getKeycloakId());

        // 2) Eliminar en Keycloak (si tiene id en KC)
        if (user.getKeycloakId() != null) {
            try {
                keycloakPort.deleteUser(user.getKeycloakId());
                log.info("✅ Usuario eliminado de Keycloak (keycloakId={})", user.getKeycloakId());
            } catch (Exception ex) {
                log.error("❌ Error eliminando usuario en Keycloak (keycloakId={})", user.getKeycloakId(), ex);
                throw ex;
            }
        } else {
            log.debug("ℹ️ Usuario id={} no tiene keycloakId asociado, solo se eliminará en BD", userId);
        }

        // 3) Eliminar en la DB
        userRepository.deleteById(userId);
        log.info("💾 Usuario eliminado de la BD local id={}", userId);
    }
}
