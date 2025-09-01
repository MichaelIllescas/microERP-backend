// application/usecase/GetProductByIdService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.port.in.GetProductByIdUseCase;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProductByIdService implements GetProductByIdUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse getById(Long id) {
        log.info("Fetching product with id={}", id);
        return productRepositoryPort.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> {
                    log.warn("Product with id={} not found", id);
                    return new IllegalArgumentException("Producto no encontrado con id " + id);
                });
    }
}
