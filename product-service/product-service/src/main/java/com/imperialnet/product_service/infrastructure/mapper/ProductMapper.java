package com.imperialnet.product_service.infrastructure.mapper;

import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.application.dto.CreateProductRequest;
import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.CategoryResponse;
import com.imperialnet.product_service.infrastructure.persistence.entity.CategoryEntity;
import com.imperialnet.product_service.infrastructure.persistence.entity.ProductEntity;


public interface ProductMapper {

    // DTO → Dominio
    Product toDomain(CreateProductRequest request);

    // Dominio ↔ Entity
    ProductEntity toEntity(Product product);
    Product toDomain(ProductEntity entity);

    Category toDomain(CategoryEntity entity);
    CategoryEntity toEntity(Category category);

    // Respuestas
    ProductResponse toResponse(Product product);
    CategoryResponse toResponse(Category category);
}
