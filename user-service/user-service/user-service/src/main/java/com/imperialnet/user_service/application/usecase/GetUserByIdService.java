// application/usecase/GetUserByIdService.java
package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.GetUserByIdUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByIdService implements GetUserByIdUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    @Override
    public UserResponse getById(Long id) {
        log.info("📥 Buscando usuario con id={}", id);

        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ Usuario no encontrado en BD con id={}", id);
                    return new RuntimeException("User not found with id: " + id);
                });

        log.info("✅ Usuario encontrado: id={} email={}", user.getId(), user.getEmail());
        return mapper.toResponse(user);
    }
}
