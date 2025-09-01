package com.imperialnet.user_service.infrastructure.mapper;

import com.imperialnet.user_service.application.dto.CreateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDomain(CreateUserRequest req) {
        if (req == null) return null;

        User user = new User();
        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            user.setUsername(req.getUsername());
        } else {
            user.setUsername(deriveUsernameFromEmail(req.getEmail()));
        }
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        // password NO está en el dominio -> se maneja en otro flujo
        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .keycloakId(user.getKeycloakId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .build();
    }

    @Override
    public User toDomainFromEntity(UserEntity entity) {
        if (entity == null) return null;

        User user = new User();
        user.setId(entity.getId());
        user.setKeycloakId(entity.getKeycloakId());
        user.setUsername(entity.getUsername());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setPhone(entity.getPhone());
        user.setStatus(entity.getStatus());
        return user;
    }

    @Override
    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setKeycloakId(user.getKeycloakId());
        entity.setUsername(user.getUsername());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setStatus(user.getStatus());
        // createdAt y updatedAt -> manejados por JPA (Auditing)
        return entity;
    }

    @Override
    public void updateEntityFromDomain(User source, UserEntity target) {
        if (source == null || target == null) return;

        if (source.getKeycloakId() != null) target.setKeycloakId(source.getKeycloakId());
        if (source.getUsername() != null) target.setUsername(source.getUsername());
        if (source.getFirstName() != null) target.setFirstName(source.getFirstName());
        if (source.getLastName() != null) target.setLastName(source.getLastName());
        if (source.getEmail() != null) target.setEmail(source.getEmail());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getStatus() != null) target.setStatus(source.getStatus());
    }

    // ==================
    // Utilidades privadas
    // ==================
    private String deriveUsernameFromEmail(String email) {
        if (email == null) return null;
        int at = email.indexOf('@');
        return (at > 0 ? email.substring(0, at) : email).toLowerCase();
    }
}
