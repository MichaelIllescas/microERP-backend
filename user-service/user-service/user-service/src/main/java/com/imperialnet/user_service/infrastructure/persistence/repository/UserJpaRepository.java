package com.imperialnet.user_service.infrastructure.persistence.repository;

import com.imperialnet.user_service.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByKeycloakId(String keycloakId);

    boolean existsByEmailAndIdNot(String email, Long id);

}
