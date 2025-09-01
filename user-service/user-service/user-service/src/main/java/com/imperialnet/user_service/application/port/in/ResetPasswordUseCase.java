package com.imperialnet.user_service.application.port.in;

public interface ResetPasswordUseCase {
    void sendResetPasswordEmail(String userId);
}