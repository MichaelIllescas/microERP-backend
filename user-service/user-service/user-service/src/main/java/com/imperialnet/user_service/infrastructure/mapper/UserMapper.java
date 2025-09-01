package com.imperialnet.user_service.infrastructure.mapper;

import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.application.dto.CreateUserRequest;
import com.imperialnet.user_service.application.dto.UserResponse;
import com.imperialnet.user_service.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.*;

public interface UserMapper {

    // 1) Request -> Dominio
    User toDomain(CreateUserRequest req);


    // 2) Dominio -> Respuesta
    UserResponse toResponse(User user);

    // 3) Entity <-> Dominio
    User toDomainFromEntity(UserEntity entity);

    // 4) Dominio -> Entity
    UserEntity toEntity(User user);


    // Utilidad para updates parciales (si decides usarlo)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDomain(User source, @MappingTarget UserEntity target);
}
