// application/usecase/ListProductsService.java
package com.imperialnet.product_service.application.usecase;

import com.imperialnet.product_service.application.dto.ProductResponse;
import com.imperialnet.product_service.application.port.in.ListProductsUseCase;
import com.imperialnet.product_service.application.port.out.ProductRepositoryPort;
import com.imperialnet.product_service.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products from repository...");

        List<ProductResponse> products = productRepositoryPort.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        log.debug("Mapped {} products to response DTOs", products.size());
        log.info("Successfully retrieved {} products", products.size());

        return products;
    }
}
