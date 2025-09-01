package com.imperialnet.user_service.application.port.in;

import com.imperialnet.user_service.application.dto.UpdateUserStatusRequest;
import com.imperialnet.user_service.application.dto.UserResponse;

public interface UpdateUserStatusUseCase {
    UserResponse updateStatus(Long id, UpdateUserStatusRequest request);
}
