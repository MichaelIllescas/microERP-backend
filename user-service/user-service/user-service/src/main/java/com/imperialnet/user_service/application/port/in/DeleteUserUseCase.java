package com.imperialnet.user_service.application.port.in;

public interface DeleteUserUseCase {
    void execute(Long userId);
}