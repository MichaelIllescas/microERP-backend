package com.imperialnet.user_service.infrastructure.persistence.adapter;

import com.imperialnet.user_service.application.port.out.UserRepositoryPort;
import com.imperialnet.user_service.domain.model.User;
import com.imperialnet.user_service.infrastructure.mapper.UserMapper;
import com.imperialnet.user_service.infrastructure.persistence.entity.UserEntity;
import com.imperialnet.user_service.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        log.info("💾 Guardando usuario en BD: email={} username={}", user.getEmail(), user.getUsername());
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        log.info("✅ Usuario guardado en BD con id={}", saved.getId());
        return userMapper.toDomainFromEntity(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("🔍 Buscando usuario en BD con id={}", id);
        return userJpaRepository.findById(id)
                .map(entity -> {
                    log.info("✅ Usuario encontrado en BD id={}", entity.getId());
                    return userMapper.toDomainFromEntity(entity);
                });
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("🔍 Buscando usuario en BD por email={}", email);
        return userJpaRepository.findByEmail(email)
                .map(entity -> {
                    log.info("✅ Usuario encontrado en BD con email={}", email);
                    return userMapper.toDomainFromEntity(entity);
                });
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        log.info("🔍 Buscando usuario en BD por keycloakId={}", keycloakId);
        return userJpaRepository.findByKeycloakId(keycloakId)
                .map(entity -> {
                    log.info("✅ Usuario encontrado en BD con keycloakId={}", keycloakId);
                    return userMapper.toDomainFromEntity(entity);
                });
    }

    @Override
    public List<User> findAll() {
        log.info("📥 Solicitando listado completo de usuarios en BD");
        List<User> users = userJpaRepository.findAll()
                .stream()
                .map(userMapper::toDomainFromEntity)
                .toList();
        log.info("✅ Se obtuvieron {} usuarios desde la BD", users.size());
        return users;
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long excludedId) {
        log.debug("🔍 Verificando si email={} ya existe en BD (excluyendo userId={})", email, excludedId);
        boolean exists = userJpaRepository.existsByEmailAndIdNot(email, excludedId);
        if (exists) {
            log.warn("⚠️ El email={} ya está en uso por otro usuario", email);
        }
        return exists;
    }

    @Override
    public void deleteById(Long id) {
        log.warn("⚠️ Eliminando usuario en BD con id={}", id);
        userJpaRepository.deleteById(id);
        log.info("✅ Usuario eliminado en BD con id={}", id);
    }
}
