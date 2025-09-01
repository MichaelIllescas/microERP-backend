package com.imperialnet.user_service.infrastructure.persistence.adapter;

import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import com.imperialnet.user_service.infrastructure.persistence.entity.UserEntity;
import com.imperialnet.user_service.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return userMapper.toDomainFromEntity(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> user= userJpaRepository.findById(id);
        return Optional.of(userMapper.toDomainFromEntity(user.get()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomainFromEntity);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return userJpaRepository.findByKeycloakId(keycloakId)
                .map(userMapper::toDomainFromEntity);
    }
    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(userMapper::toDomainFromEntity)
                .toList();
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long excludedId) {
        return userJpaRepository.existsByEmailAndIdNot(email, excludedId);
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }
}
