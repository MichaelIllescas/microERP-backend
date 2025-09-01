package com.imperialnet.product_service.infrastructure.persistence.repository;

import com.imperialnet.product_service.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
}