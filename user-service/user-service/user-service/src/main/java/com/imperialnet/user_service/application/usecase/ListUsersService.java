// application/usecase/ListUsersService.java
package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.ListUsersUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del caso de uso para listar usuarios.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ListUsersService implements ListUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> listAll() {
        log.info("📥 Solicitando listado de usuarios");

        List<User> users = userRepositoryPort.findAll();

        if (users.isEmpty()) {
            log.warn("⚠️ No se encontraron usuarios en la BD");
        } else {
            log.info("✅ Se encontraron {} usuarios en la BD", users.size());
        }

        return users.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
