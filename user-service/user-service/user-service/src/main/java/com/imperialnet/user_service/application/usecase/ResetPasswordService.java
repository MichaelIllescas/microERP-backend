package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.port.in.ResetPasswordUseCase;
import com.imperialnet.user_service.application.port.out.KeycloakUserPort;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCase {

    private final KeycloakUserPort keycloakUserPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void sendResetPasswordEmail(String userId) {
        Long userIdL = Long.parseLong(userId);
        keycloakUserPort.sendResetPasswordEmail(userRepositoryPort.findById(userIdL).get().getKeycloakId());
    }
}
