package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UpdateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.UpdateUserUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.application.exception.EmailAlreadyInUseException;
import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final KeycloakUserPort keycloakPort;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse execute(Long userId, UpdateUserRequest req) {
        log.info("✏️ Iniciando actualización de usuario id={}", userId);

        // 1) Buscar entidad
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado en BD con id={}", userId);
                    return new ResourceNotFoundException("Usuario no encontrado id=" + userId);
                });

        log.debug("Usuario actual: id={} email={} firstName={} lastName={}",
                user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());

        boolean emailChanged = req.getEmail() != null && !req.getEmail().equalsIgnoreCase(user.getEmail());

        if (emailChanged && userRepository.existsByEmailAndIdNot(req.getEmail(), userId)) {
            log.warn("❌ El email {} ya está en uso (userId={})", req.getEmail(), userId);
            throw new EmailAlreadyInUseException("El email ya está en uso: " + req.getEmail());
        }

        // 2) Aplicar cambios sobre el dominio
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhone() != null) user.setPhone(req.getPhone());

        log.debug("Usuario modificado en memoria: firstName={} lastName={} email={} phone={}",
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());

        // 3) Guardar en BD
        User updatedUser = userRepository.save(user);
        log.info("💾 Usuario actualizado en BD id={} email={}", updatedUser.getId(), updatedUser.getEmail());

        // 4) Keycloak
        try {
            if (emailChanged && updatedUser.getKeycloakId() != null) {
                keycloakPort.updateEmail(updatedUser.getKeycloakId(), updatedUser.getEmail());
                log.info("✅ Email actualizado en Keycloak para keycloakId={}", updatedUser.getKeycloakId());
            }
            if ((req.getFirstName() != null || req.getLastName() != null || req.getPhone() != null)
                    && updatedUser.getKeycloakId() != null) {
                keycloakPort.updateProfile(
                        updatedUser.getKeycloakId(),
                        updatedUser.getFirstName(),
                        updatedUser.getPhone()
                );
                log.info("✅ Perfil actualizado en Keycloak para keycloakId={}", updatedUser.getKeycloakId());
            }
        } catch (Exception ex) {
            log.error("❌ Error actualizando usuario en Keycloak (id={} keycloakId={})",
                    updatedUser.getId(), updatedUser.getKeycloakId(), ex);
            throw ex;
        }

        // 5) Respuesta
        UserResponse response = userMapper.toResponse(updatedUser);
        log.info("🎯 Actualización completada para userId={} -> email={}", response.getId(), response.getEmail());

        return response;
    }
}
