// application/usecase/UpdateProductService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.dto.UpdateProductRequest;
import com.imperialnet.product_service.application.port.in.UpdateProductUseCase;
import com.imperialnet.product_service.application.port.out.CategoryRepositoryPort;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.domain.model.Category;
import com.imperialnet.product_service.domain.model.Product;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final ProductMapper mapper;

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        log.info("Attempting to update product with id={}", id);

        Product existing = productRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with id={} not found", id);
                    return new IllegalArgumentException("Producto no encontrado con id " + id);
                });

        // ✅ Solo actualizamos si el campo no es null
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }
        if (request.getCategoryId() != null) {
            if (!categoryRepositoryPort.existsById(request.getCategoryId())) {
                throw new IllegalArgumentException("Categoría no encontrada con id " + request.getCategoryId());
            }
            existing.setCategory(Category.builder().id(request.getCategoryId()).build());
        }

        Product updated = productRepositoryPort.save(existing);
        log.info("Successfully updated product with id={}", id);

        return mapper.toResponse(updated);
    }
}
