package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.ChangePasswordRequest;

public interface ChangePasswordUseCase {
    void changePassword(Long userId, ChangePasswordRequest request);
}
