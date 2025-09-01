package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.exception.ResourceNotFoundException;
import com.imperialnet.user_service.application.port.in.DeleteUserUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserService implements DeleteUserUseCase {

    private final UserRepositoryPort userRepository;
    private final KeycloakUserPort keycloakPort;

    @Override
    @Transactional
    public void execute(Long userId) {
        // 1) Buscar usuario en DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado id=" + userId));


        // 2) Eliminar en Keycloak (si tiene id en KC)
        if (user.getKeycloakId() != null) {
            keycloakPort.deleteUser(user.getKeycloakId());
        }

        // 3) Eliminar en la DB
        userRepository.deleteById(userId);
    }
}
