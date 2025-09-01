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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final KeycloakUserPort keycloakPort;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse execute(Long userId, UpdateUserRequest req) {

        // 1) Buscar entidad
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado id=" + userId));

        boolean emailChanged = req.getEmail() != null && !req.getEmail().equalsIgnoreCase(user.getEmail());

        if (emailChanged && userRepository.existsByEmailAndIdNot(req.getEmail(), userId)) {
            throw new EmailAlreadyInUseException("El email ya está en uso: " + req.getEmail());
        }

        // 2) Aplicar cambios sobre el dominio
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getPhone() != null) user.setPhone(req.getPhone());


        User updatedUser = userRepository.save(user);

        // 5) Keycloak
        if (emailChanged && updatedUser.getKeycloakId() != null) {
            keycloakPort.updateEmail(updatedUser.getKeycloakId(), updatedUser.getEmail());
        }
        if ((req.getFirstName() != null || req.getLastName() != null || req.getPhone() != null)
                && updatedUser.getKeycloakId() != null) {
            keycloakPort.updateProfile(
                    updatedUser.getKeycloakId(),
                    updatedUser.getFirstName(),
                    updatedUser.getPhone()
            );
        }

        // 6) Respuesta
        return userMapper.toResponse(updatedUser);
    }
}
