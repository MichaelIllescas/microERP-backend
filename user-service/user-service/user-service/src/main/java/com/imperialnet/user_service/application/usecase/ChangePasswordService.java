package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.ChangePasswordRequest;
import com.imperialnet.user_service.application.port.in.ChangePasswordUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final KeycloakUserPort keycloakUserPort;

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // Buscar usuario en BD local
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Llamar a Keycloak para cambiar password
        keycloakUserPort.updatePassword(user.getKeycloakId(), request.getNewPassword());
    }
}
