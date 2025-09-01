package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.UpdateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;

public interface UpdateUserUseCase {
    UserResponse execute(Long userId, UpdateUserRequest request);
}