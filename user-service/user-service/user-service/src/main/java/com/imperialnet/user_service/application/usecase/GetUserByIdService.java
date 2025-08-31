// application/usecase/GetUserByIdService.java
package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.GetUserByIdUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdService implements GetUserByIdUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    @Override
    public UserResponse getById(Long id) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapper.toResponse(user);
    }
}
