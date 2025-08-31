// application/usecase/ListUsersService.java
package com.imperialnet.user_service.application.usecase;

import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.application.port.in.ListUsersUseCase;
import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del caso de uso para listar usuarios.
 */
@Service
@RequiredArgsConstructor
public class ListUsersService implements ListUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> listAll() {
        List<User> users = userRepositoryPort.findAll();
        return users.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
